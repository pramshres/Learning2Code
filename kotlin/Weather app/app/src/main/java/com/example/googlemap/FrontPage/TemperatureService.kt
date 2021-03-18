package com.example.googlemap.FrontPage

import com.example.googlemap.ocationForecast.locationForecast.LocForData
import retrofit2.http.GET
import retrofit2.http.Query

interface TemperatureService {

    @GET("locationforecast/1.9/.json?")
    suspend fun getTemperatureForecast(
        @Query("lat") lat: String,
        @Query("lon") long: String
    ) : LocForData
}