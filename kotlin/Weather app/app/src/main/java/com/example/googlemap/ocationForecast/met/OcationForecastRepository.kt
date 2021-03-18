package com.example.googlemap.ocationForecast.met

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.googlemap.R
import com.example.googlemap.RetrofitClient
import com.example.googlemap.ocationForecast.maps.RetrofitMaps
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OcationForecastRepository {

    private var forecastList: List<Forecast>? = null
    private lateinit var temperature: String
    private lateinit var valgtSted: LatLng


    suspend fun getSeaTemperatureValue(latLng: LatLng): MutableLiveData<List<Forecast>?> {

        getSeaTemperatureCall(latLng)
        return MutableLiveData(forecastList)
    }


    suspend fun getLandTemperatureValue(latLng: LatLng, context: Context): MutableLiveData<String?> {

        getLandTemperatureCall(latLng, context)
        return MutableLiveData(temperature)

    }


    suspend fun getLatLngFromPlace(place: Place, context: Context): MutableLiveData<LatLng> {

        fetchLatLng(place, context)
        return MutableLiveData(valgtSted)

    }


    suspend fun isLandOrSea(latLng: LatLng): MutableLiveData<Boolean> {

        return MutableLiveData(isSeaCall(latLng))

    }


    private suspend fun getSeaTemperatureCall(latLng: LatLng) {

        val service = RetrofitClient.retrofitInstance?.create(GetForecastService::class.java)
        // Kall med retrofit. Sender posisjon som param
        val body = service?.getAllForecasts(latLng.latitude.toString(), latLng.longitude.toString())
        // Dersom det er mottat et fullstendig svar

        forecastList = body?.forecast!!
    }


    private suspend fun getLandTemperatureCall(latLng: LatLng, context: Context) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(context.getString(R.string.locationUrl))
            .build()
        val service = retrofit.create(OcationLocationService::class.java)
        // Kall med retrofit. Sender posisjon som param
        val body = service.getAllForecasts(latLng.latitude.toString(), latLng.longitude.toString())

        // Dersom det er mottat et fullstendig svar
        if (body.toString().length > 1000) {

            val prod = body.product!!
            temperature = prod.time!![0].location!!.temperature!!._value

        }
    }


    private suspend fun fetchLatLng(place: Place, context: Context) {
        val service = RetrofitMaps.retrofitInstance?.create(OcationLatLngService::class.java)
        // Kall med retrofit. Sender place-id og api-nøkkel som param
        val response =
            service?.getLatLng(place.id.toString(), context.getString(R.string.places_api))


        // Respons
        val latlng = response?.component2()?.geometry?.location!!
        valgtSted = LatLng(latlng.lat.toDouble(), latlng.lng.toDouble())

    }


    private suspend fun isSeaCall(latLng: LatLng): Boolean {

        val service = RetrofitClient.retrofitInstance?.create(GetForecastService::class.java)
        val body = service?.getAllForecasts(latLng.latitude.toString(), latLng.longitude.toString())

        return if(body?.forecast?.get(0)?.oceanForecast?.seaTemperature.toString() != "null" && body.toString().length > 1000) {

            true

        } else {

            return false
        }
    }

}