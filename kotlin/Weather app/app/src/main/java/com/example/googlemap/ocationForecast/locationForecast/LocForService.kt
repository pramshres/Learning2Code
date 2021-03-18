package com.example.googlemap.ocationForecast.locationForecast

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LocForService {

    @GET("locationforecast/1.9/.json?")
    suspend fun getAllForecasts(
        @Query("lat") lat: String,
        @Query("lon") long: String
    ) : LocForData
}