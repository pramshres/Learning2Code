package com.example.googlemap.FrontPage

import com.example.googlemap.ocationForecast.maps.AddressData
import retrofit2.http.GET
import retrofit2.http.Query

interface GetAddressCoroutine {

    @GET("maps/api/geocode/json?")
    suspend fun getAddress(
        @Query("latlng") loc: String,
        @Query("key") key: String
    ) : AddressData
}