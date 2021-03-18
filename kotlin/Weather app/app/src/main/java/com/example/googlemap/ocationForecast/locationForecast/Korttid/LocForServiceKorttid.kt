package com.example.googlemap.ocationForecast.locationForecast.Korttid

import com.example.googlemap.ocationForecast.locationForecast.LocForData
import retrofit2.http.GET
import retrofit2.http.Query

interface LocForServiceKorttid {

        @GET("locationforecast/1.9/.json?")
        suspend fun getAllForecasts(
            @Query("lat") lat: String,
            @Query("lon") long: String
        ) : LocForData
}