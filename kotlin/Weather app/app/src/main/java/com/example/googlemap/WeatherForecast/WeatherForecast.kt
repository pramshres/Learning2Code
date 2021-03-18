package com.example.googlemap.WeatherForecast

import android.annotation.SuppressLint
import android.os.Bundle
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.googlemap.Temperatur.forhold.Forhold
import com.example.googlemap.R
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.android.synthetic.main.activity_weather_forecast.*


//AIzaSyCkISgPGWwhwmLDaQC60xwaULcxOkYjfW0

class   WeatherForecast : Fragment(), OnMapReadyCallback, CoroutineScope by MainScope() {

    private val weatherForecastVM: WeatherForecastViewModel by viewModels()
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: android.location.Location
    private lateinit var placesClient : PlacesClient
    private var lat = 0.0
    private var lon = 0.0
    private var stedsnavn = ""
    private lateinit var valgtSted: LatLng
    private var placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)

    // Når bruker tillater at posisjonen til enheten brukes
    companion object {

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        fun newInstance(): WeatherForecast {

            return WeatherForecast()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.activity_weather_forecast, container, false)

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        map_view_frag.onCreate(savedInstanceState)
        map_view_frag.onResume()
        map_view_frag.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        initPlaces()
        setUpAutocomplete()
        initVelgSted()

    }


    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        map.isMyLocationEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Flytter kameraet til enhetens siste kjente lokasjon
        fusedLocationClient.lastLocation.addOnSuccessListener(this.requireActivity()) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                lat = location.latitude
                lon = location.longitude
                val currentLatLng = LatLng(lat, lon)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12F))


                weatherForecastVM.getForecastAddress(currentLatLng, requireContext()).observe(this, Observer {

                    stedsnavn = it.value!!
                    setAddressMarker(currentLatLng)
                })
            }
        }
    }


    // Obligatorisk metode til kartet
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        //legger til zoom og toolbar
        val mapUiSettings = map.uiSettings
        mapUiSettings.isZoomControlsEnabled = true
        setUpMap()

        // Kalles når noen trykker på kartet
        // p0 har attributtene latitude og longitude som vi kan bruke til å hente data fra API
        map.setOnMapClickListener { p0 ->

            weatherForecastVM.getForecastAddress(LatLng(p0.latitude, p0.longitude), requireContext()).observe(this, Observer {

                lat = p0.latitude
                lon = p0.longitude
                stedsnavn = it.value!!
                setAddressMarker(LatLng(p0.latitude, p0.longitude))
            })
        }
    }


    private fun initPlaces(){
        Places.initialize(this.requireContext(), getString(R.string.places_api))
        placesClient = Places.createClient(this.requireContext())
    }


    private fun placeMarkerOnMap(location: LatLng, tittel: String): Marker {
        map.clear()
        return map.addMarker(MarkerOptions().position(location).title(tittel))
    }


    private fun initVelgSted(){
        velgAddresse.setOnClickListener {

            //val intent = Intent(this, LocForChoice::class.java)
            val intent = Intent(this.requireContext(), Forhold::class.java) //oppdatert versjon
            val sendThis = arrayListOf(lat.toString(), lon.toString(), stedsnavn)
            intent.putStringArrayListExtra("latlong", sendThis)
            startActivity(intent)
        }
    }


    private fun setUpAutocomplete(){
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.frag_places) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(placeFields)

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener{
            // Denne kalles når bruker trykker på et av autocomplete-valgene
            @SuppressLint("FragmentLiveDataObserve")
            override fun onPlaceSelected(p0: Place) {
                // Sender place-objektet som bruker har valgt videre
                stedsnavn = p0.address!!.substringBefore(",")
                velgAddresse.text = getString(R.string.vis_vaer_adresse, stedsnavn)

                weatherForecastVM.getLatLngFromPlace(p0, context!!).observe(this@WeatherForecast, Observer{

                    valgtSted = it.value!!
                    moveMapAndSetMarker()



                })
            }

            override fun onError(p0: Status) {
                Toast.makeText(requireContext(), p0.statusMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun  moveMapAndSetMarker() {

        activity?.runOnUiThread {
            placeMarkerOnMap(valgtSted, stedsnavn)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(valgtSted, 12F))
        }
    }

    private fun setAddressMarker(latLng: LatLng) {

        val marker = placeMarkerOnMap(latLng, "")

        activity?.runOnUiThread {
            // Her settes tittel på markøren som ble plassert tidligere
            marker.title = stedsnavn
            velgAddresse.text = getString(R.string.vis_vaer_adresse, stedsnavn)
        }
    }

}
