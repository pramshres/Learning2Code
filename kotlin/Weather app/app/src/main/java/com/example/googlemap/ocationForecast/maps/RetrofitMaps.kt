package com.example.googlemap.ocationForecast.maps

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitMaps {

    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://maps.googleapis.com/"

    // create a retrofit instance, only if it has not been created yet.
    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit
        }
}