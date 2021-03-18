package com.example.googlemap.Temperatur.forhold

import androidx.lifecycle.MutableLiveData
import com.example.googlemap.RetrofitClient
import com.example.googlemap.nowcast.GetNowcast
import com.example.googlemap.nowcast.met.Time
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ForholdRepository {


    private lateinit var ikon: String
    private lateinit var timeList: List<Time>

    suspend fun getIconFromLatLon(lat: String, lon: String): MutableLiveData<String> {

        getIconFromLatLonCall(lat, lon)
        return MutableLiveData(ikon)

    }


    suspend fun getTimeList(lat: String, lon: String): MutableLiveData<List<Time>> {

        getTimeListCall(lat, lon)
        return MutableLiveData(timeList)
    }


    private suspend fun getIconFromLatLonCall(lat: String, lon: String){

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://in2000-apiproxy.ifi.uio.no/weatherapi/")
            .build()
        val service = retrofit.create(ForholdLocService::class.java)

        val response = service.getIconFromLatLon(lat, lon)
        val resultater = response.component1()?.time!!
        ikon = resultater[1].location!!.symbol!!._number

    }


    private suspend fun getTimeListCall(lat: String, lon: String) {

        //bruker interfacet GetNowcast med en retrofit-klient
        val client = RetrofitClient.retrofitInstance?.create(GetNowcast::class.java)
        //call legger inn argumenter (koordinatene) til base-url

        timeList = try {
            val body = client?.getNowcast(lat, lon)
            body?.component1()?.time!!
        } catch (e: HttpException) {

            emptyList()

        }
    }

}