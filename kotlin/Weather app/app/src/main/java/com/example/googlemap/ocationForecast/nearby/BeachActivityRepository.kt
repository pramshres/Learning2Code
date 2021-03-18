package com.example.googlemap.ocationForecast.nearby

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.googlemap.R
import com.example.googlemap.ocationForecast.locationForecast.LocForService
import com.example.googlemap.ocationForecast.maps.GetLatLng
import com.example.googlemap.ocationForecast.maps.RetrofitMaps
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BeachActivityRepository {

    private lateinit var valgtSted: LatLng
    private var stedList = mutableListOf<Sted>()
    private var markerList: List<Result>? = null


    suspend fun fetchLatLng(place: Place, context: Context): MutableLiveData<LatLng> {

        fetchLatLngCall(place, context)
        return MutableLiveData(valgtSted)

    }


    suspend fun finnMarkerList(latLng: LatLng, keyword: String, context: Context): MutableLiveData<List<Result>?> {

        finnMarkerListCall(latLng, keyword, context)
        return MutableLiveData(markerList)

    }


    suspend fun hentIkonOgtemp(sted: Sted): MutableLiveData<MutableList<Sted>> {

        hentIkonOgTempCall(sted)
        return MutableLiveData(stedList)
    }


    private suspend fun fetchLatLngCall(place: Place, context: Context) {
        val service = RetrofitMaps.retrofitInstance?.create(GetLatLng::class.java)
        // Kall med retrofit. Sender place-id og api-nøkkel som param
        val body = service?.getLatLng(place.id.toString(), context.getString(R.string.places_api))


        val latlng = body?.component2()?.geometry?.location!!
        valgtSted = LatLng(latlng.lat.toDouble(), latlng.lng.toDouble())
    }

    private suspend fun finnMarkerListCall(latLng: LatLng, keyword: String, context: Context) {
        val service = RetrofitMaps.retrofitInstance?.create(GetNearbyPlaces::class.java)
        // Kall med retrofit. Sender hhv. koordinater, radius, nøkkelord og api-nøkkel som param
        val call = service?.getNearbyPlaces("${latLng.latitude},${latLng.longitude}", "5000", keyword, context.getString(R.string.places_api))

        // Respons
        markerList = call?.component1()

    }


    private suspend fun hentIkonOgTempCall(sted: Sted){
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://in2000-apiproxy.ifi.uio.no/weatherapi/")
            .build()
        val service = retrofit.create(LocForService::class.java)

        // Kall med retrofit. Sender posisjon som param
        val call = service.getAllForecasts(sted.pos.latitude.toString(), sted.pos.longitude.toString())

        // Respons
        val resultater = call.component1()?.time!!
        sted.ikonId = resultater[1].location!!.symbol!!._number
        sted.temp = resultater[0].location!!.temperature!!._value
        sted.weather = resultater[0].location!!.windSpeed!!._name
        sted.vindRetning = resultater[0].location!!.windDirection!!._name
        sted.vindStyrke = resultater[0].location!!.windSpeed!!._mps
        stedList.add(sted)


    }

}