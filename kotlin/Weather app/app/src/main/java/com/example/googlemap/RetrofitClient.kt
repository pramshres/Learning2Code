package com.example.googlemap

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Retrofit-klient som benyttes mange steder i koden
object RetrofitClient {

    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://in2000-apiproxy.ifi.uio.no/"

    // oppretter en retrofit instanse om det ikke finnes en fra før
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