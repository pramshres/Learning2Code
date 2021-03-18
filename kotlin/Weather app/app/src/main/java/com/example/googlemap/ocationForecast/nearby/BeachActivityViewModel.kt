package com.example.googlemap.ocationForecast.nearby

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.Dispatchers

class BeachActivityViewModel: ViewModel() {


    fun fetchLatLng(place: Place, context: Context) = liveData(Dispatchers.IO) {

        val data = BeachActivityRepository.fetchLatLng(place, context)
        emit(data)

    }


    fun finnMarkerList(latLng: LatLng, keyword: String, context: Context) = liveData(Dispatchers.IO) {

        val data = BeachActivityRepository.finnMarkerList(latLng, keyword, context)
        emit(data)

    }

    fun hentIkonOgTemp(sted: Sted) = liveData(Dispatchers.IO) {

        val data = BeachActivityRepository.hentIkonOgtemp(sted)
        emit(data)

    }




}