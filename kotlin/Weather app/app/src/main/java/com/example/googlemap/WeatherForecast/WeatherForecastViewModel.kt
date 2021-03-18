package com.example.googlemap.WeatherForecast

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.Dispatchers

class WeatherForecastViewModel: ViewModel() {


    fun getForecastAddress(latLng: LatLng, context: Context) = liveData(Dispatchers.IO) {

        val data = WeatherForecastRepository.getForecastAddress(latLng, context)
        emit(data)
    }


    fun getLatLngFromPlace(place: Place, context: Context) = liveData(Dispatchers.IO) {

        val data = WeatherForecastRepository.getLatLngFromPlace(place, context)
        emit(data)
    }



}