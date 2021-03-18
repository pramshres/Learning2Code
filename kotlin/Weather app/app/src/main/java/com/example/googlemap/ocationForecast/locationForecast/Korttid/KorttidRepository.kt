package com.example.googlemap.ocationForecast.locationForecast.Korttid

import androidx.lifecycle.MutableLiveData
import com.example.googlemap.ocationForecast.locationForecast.Time
import com.example.googlemap.ocationForecast.locationForecast.Weather
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KorttidRepository {

    private const val baseUrl = "https://in2000-apiproxy.ifi.uio.no/weatherapi/"
    private lateinit var forecastList: List<Time>
    private var timeForTime: ArrayList<Weather> = ArrayList()


    suspend fun getForecastList(lat: String, lon: String): MutableLiveData<ArrayList<Weather>> {

        timeForTime.clear()
        getForecastCall(lat, lon)
        return MutableLiveData(timeForTime)

    }


    private suspend fun getForecastCall(lat: String, lon: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
        val service = retrofit.create(LocForServiceKorttid::class.java)
        val body = service.getAllForecasts(lat, lon)

        if (body.toString().length > 1000) {
            val prod = body.product!!
            forecastList = prod.time!!

            forecastList.forEachIndexed { index, _ ->
                val tid = forecastList[index]._to
                var dato = ""
                var kl = ""
                var dateDone = false
                for (char in tid) {

                    if (char == 'T') {
                        dateDone = true
                    }

                    if (!dateDone) {
                        dato += char
                    }
                    if (dateDone && char != 'T') {
                        if (char == ':') {
                            break
                        }
                        kl += char
                    }


                }

                val klInt = kl.toInt()

                if (index == 0 || index == 1) {
                    //skip, det er timen før
                }
                else if (index == 2) {
                    val mWeather = Weather()

                    mWeather.dato = dato
                    mWeather.kl = kl

                    if (forecastList[index].location!!.temperature != null) {
                        mWeather.temperatureUnit =
                            forecastList[index].location!!.temperature!!._unit
                        mWeather.temperatureValue =
                            forecastList[index].location!!.temperature!!._value
                    }
                    if (forecastList[index].location!!.precipitation != null) {
                        mWeather.precipitationValue =
                            forecastList[index].location!!.precipitation!!._value
                        mWeather.precipitationUnit =
                            forecastList[index].location!!.precipitation!!._unit
                    }
                    if (forecastList[index].location!!.windDirection != null) {
                        mWeather.windDirectionDeg =
                            forecastList[index].location!!.windDirection!!._deg
                        mWeather.windDirectionName =
                            forecastList[index].location!!.windDirection!!._name
                    }
                    if (forecastList[index].location!!.windSpeed != null) {
                        mWeather.windSpeedBeaufort =
                            forecastList[index].location!!.windSpeed!!._beaufort
                        mWeather.windSpeedMps = forecastList[index].location!!.windSpeed!!._mps
                        mWeather.windSpeedName = forecastList[index].location!!.windSpeed!!._name
                    }
                    if (forecastList[index].location!!.symbol != null) {
                        mWeather.symbolId = forecastList[index].location!!.symbol!!._id
                        mWeather.symbolNumber = forecastList[index].location!!.symbol!!._number
                    }
                    timeForTime.add(mWeather)
                } else {
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
                            if (forecastList[index - 1].location!!.precipitation == null && forecastList[index].location!!.precipitation != null) {
                                timeForTime[indeks].precipitationValue =
                                    forecastList[index].location!!.precipitation!!._value
                                timeForTime[indeks].precipitationUnit =
                                    forecastList[index].location!!.precipitation!!._unit
                            }

                            if (forecastList[index - 1].location!!.windDirection == null && forecastList[index].location!!.windDirection != null) {
                                timeForTime[indeks].windDirectionDeg =
                                    forecastList[index].location!!.windDirection!!._deg
                                timeForTime[indeks].windDirectionName =
                                    forecastList[index].location!!.windDirection!!._name
                            }

                            if (forecastList[index - 1].location!!.windSpeed == null && forecastList[index].location!!.windSpeed != null) {
                                timeForTime[indeks].windSpeedBeaufort =
                                    forecastList[index].location!!.windSpeed!!._beaufort
                                timeForTime[indeks].windSpeedMps =
                                    forecastList[index].location!!.windSpeed!!._mps
                                timeForTime[indeks].windSpeedName =
                                    forecastList[index].location!!.windSpeed!!._name
                            }
                            if (forecastList[index - 1].location!!.symbol == null && forecastList[index].location!!.symbol != null) {
                                timeForTime[indeks].symbolId =
                                    forecastList[index].location!!.symbol!!._id
                                timeForTime[indeks].symbolNumber =
                                    forecastList[index].location!!.symbol!!._number
                            }
                        }
                    }

                    if (timeForTime[timeForTime.size - 1].kl.toInt() != klInt) {
                        val mWeather =
                            Weather()
                        mWeather.dato = dato
                        mWeather.kl = kl
                        if (forecastList[index].location!!.temperature != null) {
                            mWeather.temperatureUnit =
                                forecastList[index].location!!.temperature!!._unit
                            mWeather.temperatureValue =
                                forecastList[index].location!!.temperature!!._value
                        }
                        if (forecastList[index].location!!.precipitation != null) {
                            mWeather.precipitationValue =
                                forecastList[index].location!!.precipitation!!._value
                            mWeather.precipitationUnit =
                                forecastList[index].location!!.precipitation!!._unit
                        }
                        if (forecastList[index].location!!.windDirection != null) {
                            mWeather.windDirectionDeg =
                                forecastList[index].location!!.windDirection!!._deg
                            mWeather.windDirectionName =
                                forecastList[index].location!!.windDirection!!._name
                        }
                        if (forecastList[index].location!!.windSpeed != null) {
                            mWeather.windSpeedBeaufort =
                                forecastList[index].location!!.windSpeed!!._beaufort
                            mWeather.windSpeedMps = forecastList[index].location!!.windSpeed!!._mps
                            mWeather.windSpeedName =
                                forecastList[index].location!!.windSpeed!!._name
                        }
                        if (forecastList[index].location!!.symbol != null) {
                            mWeather.symbolId = forecastList[index].location!!.symbol!!._id
                            mWeather.symbolNumber = forecastList[index].location!!.symbol!!._number
                        }
                        timeForTime.add(mWeather)
                    }
                }
            }
        }
    }
}