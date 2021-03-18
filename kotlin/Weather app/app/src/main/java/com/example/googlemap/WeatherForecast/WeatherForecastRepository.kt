package com.example.googlemap.WeatherForecast

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.googlemap.R
import com.example.googlemap.ocationForecast.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place

object WeatherForecastRepository {

    private var stedsnavn = ""
    private lateinit var valgtSted: LatLng


    suspend fun getForecastAddress(latLng: LatLng, context: Context) : MutableLiveData<String> {

        getForecastAddressCall(latLng, context)
        return MutableLiveData(stedsnavn)
    }


    suspend fun getLatLngFromPlace(place: Place, context: Context): MutableLiveData<LatLng> {

        getLatLngFromPlaceCall(place, context)
        return MutableLiveData(valgtSted)

    }


    // Henter adresse med api-kall
    private suspend fun getForecastAddressCall(latLng: LatLng, context: Context) {

        // Dette gjør at man ikke må vente med å plassere markøren til api-kallet er ferdig

        val service = RetrofitMaps.retrofitInstance?.create(WeatherForecastService::class.java)
        // Kall med retrofit. Sender koordinater og api-nøkkel som param
        val response = service?.getForecastAddress("${latLng.latitude},${latLng.longitude}", context.getString(R.string.places_api)
        )

        val resultater = response!!.component1()
        if (resultater!!.isNotEmpty()) {


            stedsnavn = resultater[0].formatted_address!!.substringBefore(",")

        }
    }


    private suspend fun getLatLngFromPlaceCall(place: Place, context: Context) {
        val service = RetrofitMaps.retrofitInstance?.create(WeatherForecastLatLangService::class.java)
        // Kall med retrofit. Sender place-id og api-nøkkel som param
        val response = service?.getLatLng(place.id.toString(), context.getString(R.string.places_api))


        // Respons
        val koord = response?.component2()?.geometry?.location!!
        valgtSted = LatLng(koord.lat.toDouble(), koord.lng.toDouble())

    }

}