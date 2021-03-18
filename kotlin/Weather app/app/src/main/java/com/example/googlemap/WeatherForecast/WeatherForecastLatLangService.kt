package com.example.googlemap.WeatherForecast

import com.example.googlemap.ocationForecast.maps.MapsData
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherForecastLatLangService {

    @GET("maps/api/place/details/json?")
    suspend fun getLatLng(
        @Query("place_id") placeId:String,
        @Query("key") apiKey: String
    ) : MapsData

}