package com.example.googlemap.Temperatur.forhold

import com.example.googlemap.ocationForecast.locationForecast.LocForData
import retrofit2.http.GET
import retrofit2.http.Query

interface ForholdLocService {

    @GET("locationforecast/1.9/.json?")
    suspend fun getIconFromLatLon(
        @Query("lat") lat: String,
        @Query("lon") long: String
    ) : LocForData

}