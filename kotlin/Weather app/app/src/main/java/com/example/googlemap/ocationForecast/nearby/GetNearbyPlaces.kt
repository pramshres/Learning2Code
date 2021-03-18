package com.example.googlemap.ocationForecast.nearby

import retrofit2.http.GET
import retrofit2.http.Query

interface GetNearbyPlaces {

    @GET("maps/api/place/nearbysearch/json?")
    suspend fun getNearbyPlaces(
        @Query("location") loc: String,
        @Query("radius") rad: String,
        @Query("keyword") keyword: String,
        @Query("key") key: String
    ) : NearbyData
}