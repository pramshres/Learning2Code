package com.example.googlemap.ocationForecast.met

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.Dispatchers

class OcationForecastViewModel: ViewModel() {

    fun getSeaTemperatureCall(latLng: LatLng, context: Context) = liveData(Dispatchers.IO) {

        val data = OcationForecastRepository.getSeaTemperatureValue(latLng)
        emit(data)
    }


    fun getLandTemperatureCall(latLng: LatLng, context: Context) = liveData(Dispatchers.IO) {

        val data = OcationForecastRepository.getLandTemperatureValue(latLng, context)
        emit(data)
    }


    fun getLatLngFromPlace(place: Place, context: Context) = liveData(Dispatchers.IO) {

        val data = OcationForecastRepository.getLatLngFromPlace(place, context)
        emit(data)
    }


    fun isLandOrSea(latlng: LatLng, context: Context) = liveData(Dispatchers.IO) {

        val data = OcationForecastRepository.isLandOrSea(latlng)
        emit(data)

    }

}