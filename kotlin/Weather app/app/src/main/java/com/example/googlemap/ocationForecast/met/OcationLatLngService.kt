package com.example.googlemap.ocationForecast.met

import com.example.googlemap.ocationForecast.maps.MapsData
import retrofit2.http.GET
import retrofit2.http.Query

interface OcationLatLngService {

    @GET("maps/api/place/details/json?")
    suspend fun getLatLng(
        @Query("place_id") placeId:String,
        @Query("key") apiKey: String
    ) : MapsData
}