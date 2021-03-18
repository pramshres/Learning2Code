package com.example.googlemap.ocationForecast.locationForecast.Korttid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

class KorttidViewModel: ViewModel() {


    fun getForecastList(lat: String, lon: String) = liveData(Dispatchers.IO) {

        val data = KorttidRepository.getForecastList(lat, lon)
        emit(data)

    }

}