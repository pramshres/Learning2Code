package com.example.googlemap.nowcast

import com.example.googlemap.nowcast.met.Nowcast
import retrofit2.http.GET
import retrofit2.http.Query

interface GetNowcast {

    @GET("weatherapi/nowcast/0.9/.json?")
    suspend fun getNowcast(
        @Query("lat") latitude : String,
        @Query("lon") longitude : String
    ) : Nowcast

}