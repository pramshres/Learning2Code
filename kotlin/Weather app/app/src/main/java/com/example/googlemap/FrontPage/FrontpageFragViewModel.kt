package com.example.googlemap.FrontPage

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers

class FrontpageFragViewModel: ViewModel() {

    fun fetchTemp(lat: String, long: String) = liveData(Dispatchers.IO) {

        val data = FrontPageFragRepo.fetchTemps(lat, long)
        emit(data)
    }


    fun getAddressCall(latlng: LatLng, context: Context) = liveData(Dispatchers.IO) {

        val data = FrontPageFragRepo.getAddress(latlng, context)
        emit(data)

    }

}