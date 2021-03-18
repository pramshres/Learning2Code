package com.example.googlemap.ocationForecast.maps

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetLatLng {

    @GET("maps/api/place/details/json?")
    suspend fun getLatLng(
        @Query("place_id") placeId:String,
        @Query("key") apiKey: String
    ) : MapsData
}