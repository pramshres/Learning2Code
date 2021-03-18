package com.example.googlemap.Temperatur.forhold

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

class ForholdViewModel: ViewModel() {


    fun getIkonFromLatLon(lat: String, lon: String) = liveData(Dispatchers.IO) {


        val data = ForholdRepository.getIconFromLatLon(lat, lon)
        emit(data)

    }


    fun getTimeList(lat: String, lon: String) = liveData(Dispatchers.IO) {

        val data = ForholdRepository.getTimeList(lat, lon)
        emit(data)

    }
}