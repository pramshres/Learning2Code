package com.example.googlemap.FrontPage

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.googlemap.R
import com.example.googlemap.ocationForecast.locationForecast.Time
import com.example.googlemap.ocationForecast.locationForecast.Weather
import com.example.googlemap.ocationForecast.maps.RetrofitMaps
import com.google.android.gms.maps.model.LatLng
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

object FrontPageFragRepo {

    private const val baseUrl = "https://in2000-apiproxy.ifi.uio.no/weatherapi/"
    private lateinit var forecastList: List<Time>
    private var timeForTime: ArrayList<Weather> = ArrayList()
    private var stedsnavn: String? = null


    suspend fun fetchTemps(lat: String, lon: String): MutableLiveData<ArrayList<Weather>> {

        this.fetchTemp(lat,lon)
        return MutableLiveData(timeForTime)
    }


     suspend fun getAddress(latLng: LatLng, context: Context): MutableLiveData<String?> {

        visAddresse(latLng, context)
        return MutableLiveData(stedsnavn)
    }


    private suspend fun fetchTemp(lat: String, lon: String){
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
        val service = retrofit.create(TemperatureService::class.java)
        val body = service.getTemperatureForecast(lat,lon)

        if(body.toString().length > 1000){
            val prod = body.product!!
            forecastList = prod.time!!

            forecastList.forEachIndexed { index, _ ->
                val tid = forecastList[index]._to
                var dato = ""
                var kl = ""
                var dateDone = false
                for (char in tid){
                    if (char == 'T'){
                        dateDone = true
                    }
                    if (!dateDone){
                        dato += char
                    }
                    if (dateDone && char != 'T'){
                        if (char == ':'){
                            break
                        }
                        kl += char
                    }
                }
                val klInt = kl.toInt()

                if (index == 0){
                    val mWeather = Weather()
                    mWeather.dato = dato
                    mWeather.kl = kl
                    if (forecastList[index].location!!.temperature != null){
                        mWeather.temperatureUnit = forecastList[index].location!!.temperature!!._unit
                        mWeather.temperatureValue = forecastList[index].location!!.temperature!!._value
                    }
                    timeForTime.add(mWeather)
                }

                else {
                    //hvis denne ikke er dagsvær 8-14
                    if (forecastList[index].location!!.maxTemperature == null) {

                        //hvis tidspunkt mellom forrige og denne er lik
                        if (forecastList[index - 1]._to == forecastList[index]._to) {
                            val indeks = timeForTime.size - 1
                            if (forecastList[index - 1].location!!.temperature == null && forecastList[index].location!!.temperature != null) {
                                timeForTime[indeks].temperatureUnit =
                                    forecastList[index].location!!.temperature!!._unit
                                timeForTime[indeks].temperatureValue =
                                    forecastList[index].location!!.temperature!!._value
                            }
                        }
                    }

                    if (timeForTime[timeForTime.size - 1].kl.toInt()!=klInt){
                        val mWeather = Weather()
                        mWeather.dato=dato
                        mWeather.kl=kl
                        if (forecastList[index].location!!.temperature != null){
                            mWeather.temperatureUnit = forecastList[index].location!!.temperature!!._unit
                            mWeather.temperatureValue = forecastList[index].location!!.temperature!!._value
                        }
                        timeForTime.add(mWeather)
                    }
                }
            }
        }
    }


    private suspend fun visAddresse(latLng: LatLng, context: Context){
        println(latLng.toString())
        val service = RetrofitMaps.retrofitInstance?.create(GetAddressCoroutine::class.java)
        // Kall med retrofit. Sender place-id og api-nøkkel som param
        val response = service?.getAddress("${latLng.latitude},${latLng.longitude}", context.getString(R.string.places_api))!!

        stedsnavn = if(response.component1()?.get(0) == null) {

            "Kunne ikke finne addressen!"

        } else {

            response.component1()?.get(0)?.formatted_address?.substringBefore(",")

        }
    }

}