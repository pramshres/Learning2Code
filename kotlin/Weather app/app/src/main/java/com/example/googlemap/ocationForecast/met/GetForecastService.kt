package com.example.googlemap.ocationForecast.met


import retrofit2.http.GET
import retrofit2.http.Query

interface GetForecastService {

    @GET("weatherapi/oceanforecast/0.9/.json?")
    suspend fun getAllForecasts(
        @Query("lat") latitude:String,
        @Query("lon") longitude: String
    ) : Data
}