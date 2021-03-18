package com.example.googlemap.WeatherForecast

import com.example.googlemap.ocationForecast.maps.AddressData
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherForecastService {

    @GET("maps/api/geocode/json?")
    suspend fun getForecastAddress(
        @Query("latlng") loc: String,
        @Query("key") key: String
    ) : AddressData
}