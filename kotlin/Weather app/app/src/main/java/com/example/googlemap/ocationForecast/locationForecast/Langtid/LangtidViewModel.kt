package com.example.googlemap.ocationForecast.locationForecast.Langtid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

class LangtidViewModel: ViewModel() {

    fun getForecastList(lat: String, lon: String) = liveData(Dispatchers.IO) {

        val data = LangtidRepository.getForecastList(lat, lon)
        emit(data)

    }

}