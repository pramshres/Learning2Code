package com.example.googlemap.ocationForecast.met

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.googlemap.R
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.android.synthetic.main.activity_ocation_forecast.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope


class OcationForecast : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, CoroutineScope by MainScope() {

    private val ocationForecastVM: OcationForecastViewModel by viewModels()
    private lateinit var map: GoogleMap
    private var forecastList: List<Forecast>? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: android.location.Location
    private lateinit var placesClient: PlacesClient
    private var placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)

    //settings
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPref = "mypref"
    private val storTekst = "storTekst"
    private val fargefilter = "fargefilter"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //settings
        sharedPreferences = getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
        val bol = sharedPreferences.getBoolean(fargefilter, false)
        val tekst = sharedPreferences.getBoolean(storTekst, false)

        if (!bol){
            if (tekst){
                setTheme(R.style.AppThemeBig)
            }
            else {
                setTheme(R.style.AppTheme)
            }
        }
        else {
            if (tekst){
                setTheme(R.style.bwBig)
            }
            else {
                setTheme(R.style.bw)
            }
        }
        setContentView(R.layout.activity_ocation_forecast)

        initMap()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@OcationForecast)
        initPlaces()
        setUpAutocomplete()
    }

    /*
   <------------------------------------------------ Setup ------------------------------------------------->
    */

    // Når bruker tillater at posisjonen til enheten brukes
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    // Initiere kart
    private fun initMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // Sett opp Google Places
    private fun initPlaces() {
        Places.initialize(this, getString(R.string.places_api))
        placesClient = Places.createClient(this)
    }

    // Sett opp autocomplete
    private fun setUpAutocomplete() {
        val autocompleteFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_places) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(placeFields)

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {

            // Denne kalles når bruker trykker på et av autocomplete-valgene
            override fun onPlaceSelected(p0: Place) {

                setTemperature(p0)

            }

            override fun onError(p0: Status) {
                Toast.makeText(this@OcationForecast, p0.statusMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    // Obligatorisk metode til kartet
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        //legger til zoom og toolbar
        val mapUiSettings = map.uiSettings
        mapUiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        setUpMap()
        map.setOnMapClickListener { p0 ->

            setTemperature(p0)

        }
    }

    // Henter info om brukers lokasjon dersom bruker har tillat det.
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        map.isMyLocationEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Flytter kameraet til enhetens siste kjente lokasjon
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLng, "Din plassering")
                setTemperature(currentLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12F))
            }
        }
    }


    /*
<------------------------------------------------ Metoder ------------------------------------------------->
*/

    private fun placeMarkerOnMap(location: LatLng, tittel: String): Marker {
        return map.addMarker(MarkerOptions().position(location).title(tittel))
    }

    private fun visTempHav(latLng: LatLng){

        ocationForecastVM.getSeaTemperatureCall(latLng, applicationContext).observe(this, Observer{

            forecastList = it.value!!

            map.clear()
            placeMarkerOnMap(latLng, "")

            runOnUiThread {

                val temp = forecastList?.get(0)?.oceanForecast?.seaTemperature?.content.toString()
                visTemperatur.text = getString(R.string.vis_tempHav, temp)
            }
        })

    }

    private fun visTempLand(latLng: LatLng){

        ocationForecastVM.getLandTemperatureCall(latLng, applicationContext).observe(this, Observer{

            map.clear()
            placeMarkerOnMap(latLng, "")
            runOnUiThread {

                visTemperatur.text = getString(R.string.vis_tempLand, it.value!!)

            }
        })
    }

    private fun setTemperature(latLng: LatLng) {

        ocationForecastVM.isLandOrSea(latLng, applicationContext).observe(this, Observer{

            val isSea = it.value!!

            if(isSea) {

                    visTempHav(latLng)
            } else {

                    visTempLand(latLng)
            }
        })

    }

    private fun setTemperature(place: Place) {

        ocationForecastVM.getLatLngFromPlace(place, applicationContext).observe(this, Observer {

            val latLng = it.value!!

            runOnUiThread {

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12F))
            }

            setTemperature(latLng)
        })
    }




    /*
    <------------------------------------------------ API-kall ------------------------------------------------->
     */
    override fun onMarkerClick(p0: Marker?) = false
    // Kalles bare når søkebaren benyttes

}
