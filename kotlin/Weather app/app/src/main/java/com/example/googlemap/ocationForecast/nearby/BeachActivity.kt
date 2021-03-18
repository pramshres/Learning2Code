package com.example.googlemap.ocationForecast.nearby

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.googlemap.R
import com.example.googlemap.ocationForecast.locationForecast.LocForData
import com.example.googlemap.ocationForecast.locationForecast.LocForService
import com.example.googlemap.ocationForecast.maps.GetLatLng
import com.example.googlemap.ocationForecast.maps.MapsData
import com.example.googlemap.ocationForecast.maps.RetrofitMaps
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
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
import kotlinx.android.synthetic.main.activity_beach.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//AIzaSyCkISgPGWwhwmLDaQC60xwaULcxOkYjfW0

class BeachActivity : AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    CoroutineScope by MainScope(),
    GoogleMap.InfoWindowAdapter{


    private val beachActivityVM: BeachActivityViewModel by viewModels()
    private var placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var location: Location
    private lateinit var placesClient : PlacesClient
    private lateinit var keyword: String
    private lateinit var window: View
    private lateinit var contents: View
    private lateinit var locationlatLng: LatLng
    private var steder = mutableListOf<Sted>()

    //settings
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPref = "mypref"
    private val storTekst = "storTekst"
    private val fargefilter = "fargefilter"


    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater: LayoutInflater = LayoutInflater.from(this)
        window = inflater.inflate(R.layout.custom_info_window, null)
        contents = inflater.inflate(R.layout.custom_info_contents, null)

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
        setContentView(R.layout.activity_beach)
        handleIntent()

        initMap()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initPlaces()
        setUpAutocomplete()

        finnStrenderKnapp()
    }

    /*
   <------------------------------------------------ Setup ------------------------------------------------->
    */

    // Når bruker tillater at posisjonen til enheten brukes
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    // Hente info fra intent
    private fun handleIntent(){
        val intent = intent
        // Henter keyword
        val melding = intent.extras!!.getStringArrayList("keyword")!!
        keyword = melding[0]

        val res: Resources = resources
        // Henter array med norske navn på de tilhørende keyword-ene
        val aktiviteter = res.getStringArray(R.array.aktivitet)
        finnStrender.text = aktiviteter[melding[1].toInt()]
    }

    // Initiere kart
    private fun initMap(){
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // Sett opp Google Places
    private fun initPlaces(){
        Places.initialize(this, getString(R.string.places_api))
        placesClient = Places.createClient(this)
    }

    // Sett opp autocomplete
    private fun setUpAutocomplete(){
        val autocompleteFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_places) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(placeFields)

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener{
            // Denne kalles når bruker trykker på et av autocomplete-valgene
            override fun onPlaceSelected(p0: Place) {
                // Sender place-objektet som bruker har valgt videre


                forwardPlace(p0)
            }

            override fun onError(p0: Status) {
                Toast.makeText(this@BeachActivity, p0.statusMessage, Toast.LENGTH_LONG).show()
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

        // Kalles når noen trykker på kartet
        // p0 har attributtene latitude og longitude som vi kan bruke til å hente data fra API
    }

    // Henter info om brukers lokasjon dersom bruker har tillat det.
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        map.isMyLocationEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Flytter kameraet til enhetens siste kjente lokasjon

        val lm = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        location  = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
        locationlatLng = LatLng(location.latitude, location.longitude)
        placeMarkerOnMap(locationlatLng, "Din plassering")
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(locationlatLng, 12F))

        beachActivityVM.finnMarkerList(locationlatLng, keyword, applicationContext).observe(this, Observer { markerListLambda->


            addListOfMarkers(markerListLambda)

            beachActivityVM.hentIkonOgTemp(Sted(locationlatLng)).observe(this, Observer{stederLambda ->

                steder = stederLambda.value!!

            })
        })

        map.setInfoWindowAdapter(this)
    }

    // Setter opp knapp
    private fun finnStrenderKnapp(){

        finnStrender.setOnClickListener {

            beachActivityVM.finnMarkerList(locationlatLng, keyword, applicationContext).observe(this, Observer {


                addListOfMarkers(it)

            })
        }
    }

    /*
    <------------------------------------------------ API-kall ------------------------------------------------->
     */

    // Finner strender i nærheten til en spesifikk lokasjon


    // Henter latitude og longitude ut ifra id-en til Place-objektet med retrofit.


    // Henter diverse værdata og ikon-referanse og legger det i et Sted-objekt
    // Stedet legges så i en mutableListOf<Sted>




    /*
 <------------------------------------------------ Custom Marker-Window ------------------------------------------------->
  */

    private fun placeMarkerOnMap(location: LatLng, navn: String): Marker {
        return map.addMarker(MarkerOptions().position(location).title(navn))
    }

    override fun onMarkerClick(p0: Marker) = false

    override fun getInfoContents(p0: Marker): View? {
        println(p0.title)
        val markerTitle: String? = p0.title
        val viewTitle = window.findViewById<TextView>(R.id.markerTitleContent)
        viewTitle.text = markerTitle
        return contents
    }

    // Egendefinert info-vindu
    override fun getInfoWindow(p0: Marker): View? {
        println(p0.title)

        // Henter elementene i vinduet (layout-filen custum_info_window) som skal defineres
        val viewTitle = window.findViewById<TextView>(R.id.markerTitleWindow)
        val viewSnippet1 = window.findViewById<TextView>(R.id.markerSnippetWindow1)
        val viewSnippet2 = window.findViewById<TextView>(R.id.markerSnippetWindow2)
        val visBilde = window.findViewById<ImageView>(R.id.beachIcon)
        var ikon = ""

        // Setter tittel på infovindu
        val markerTitle: String? = p0.title
        viewTitle.text = markerTitle

        if(steder.isNotEmpty()){
            for (i in steder.indices){
                // Finner sted-objektet som hører til denne markøren (De har samme posisjon)
                if (p0.position == steder[i].pos){
                    // Henter ikonId
                    ikon = steder[i].ikonId
                    // Setter tekst med riktigie vær-data til infovinduet
                    viewSnippet1.text = getString(R.string.kort_melding1, steder[i].temp)
                    viewSnippet2.text = getString(R.string.kort_melding2, steder[i].weather, steder[i].vindStyrke, steder[i].hentVindRetning())
                }
            }
        }
        // Loader ikon som svg
        visBilde.setImageResource(android.R.color.transparent)
        val uri = loadWeatherIconSvg(ikon)
        GlideToVectorYou.justLoadImage(this, uri, visBilde)
        Glide.with(this).load(uri.toString()).dontTransform().into(visBilde)
        return window
    }

    private fun loadWeatherIconSvg(symbol: String): Uri {
        val baseURL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/weathericon/1.1"
        val product = "?symbol=$symbol&content_type=image/svg%2Bxml"
        return Uri.parse("$baseURL$product")
    }


    private fun forwardPlace(place: Place) {


        beachActivityVM.fetchLatLng(place, applicationContext).observe(this, Observer { latlngLambda ->

            val position = latlngLambda.value!!


            beachActivityVM.finnMarkerList(position, keyword, applicationContext).observe(this, Observer{ markerListLambda ->

                map.clear()
                addListOfMarkers(markerListLambda)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 12F))
            })

        })
    }


    private fun addListOfMarkers(it: MutableLiveData<List<Result>?>) {

        for (i in it.value!!.indices) {

            val pos = LatLng(it.value!![i].geometry?.location?.lat!!.toDouble(), it.value!![i].geometry?.location?.lng!!.toDouble())
            val navn = it.value!![i].name!!
            placeMarkerOnMap(pos, navn)

            beachActivityVM.hentIkonOgTemp(Sted(pos)).observe(this, Observer{ stederLambda ->

                steder = stederLambda.value!!

            })

        }
    }
}

