package com.example.googlemap.ocationForecast.locationForecast.Langtid

import androidx.lifecycle.MutableLiveData
import com.example.googlemap.ocationForecast.locationForecast.Time
import com.example.googlemap.ocationForecast.locationForecast.Weather
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LangtidRepository {


    private const val baseUrl = "https://in2000-apiproxy.ifi.uio.no/weatherapi/"
    private lateinit var forecastList: List<Time>
    private var langtid: ArrayList<Weather> = ArrayList()
    private var sisteTil = 0
    private var sisteDato = ""
    private var nyDag = false


    suspend fun getForecastList(lat: String, lon: String): MutableLiveData<ArrayList<Weather>> {

        langtid.clear()
        getForecastCall(lat, lon)
        return MutableLiveData(langtid)

    }


    private suspend fun getForecastCall(lat: String, lon: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
        val service = retrofit.create(LocForServiceLangtid::class.java)
        val body = service.getAllForecasts(lat, lon)


        if (body.toString().length > 1000) {
            val prod = body.product!!
            forecastList = prod.time!!

            forecastList.forEachIndexed { index, _ ->
                //til
                val til = forecastList[index]._to
                var datoTil = ""
                var klTil = ""
                var dateDone = false
                for (char in til) {
                    if (char == 'T') {
                        dateDone = true
                    }
                    if (!dateDone) {
                        datoTil += char
                    }
                    if (dateDone && char != 'T') {
                        if (char == ':') {
                            break
                        }
                        klTil += char
                    }
                }
                val klTilInt = klTil.toInt()

                //fra
                val fra = forecastList[index]._from
                var datoFra = ""
                var klFra = ""
                dateDone = false
                for (char in fra) {
                    if (char == 'T') {
                        dateDone = true
                    }
                    if (!dateDone) {
                        datoFra += char
                    }
                    if (dateDone && char != 'T') {
                        if (char == ':') {
                            break
                        }
                        klFra += char
                    }
                }
                val klFraInt = klFra.toInt()

                if (klTilInt - klFraInt > 2) { //hvis langtid

                    when { //hvis første
                        langtid.size == 0 -> {
                            sisteTil = klTilInt
                            sisteDato = datoFra
                            val mWeather =
                                Weather()
                            mWeather.fraDato = datoFra
                            mWeather.datoVise = datoFra
                            mWeather.tilDato = datoTil
                            mWeather.fra = klFra
                            mWeather.til = klTil
                            if (forecastList[index].location!!.minTemperature != null) {
                                mWeather.minTemp =
                                    forecastList[index].location!!.minTemperature!!._value
                            }
                            if (forecastList[index].location!!.maxTemperature != null) {
                                mWeather.maxTemp =
                                    forecastList[index].location!!.maxTemperature!!._value
                            }
                            if (forecastList[index].location!!.precipitation != null) {
                                mWeather.precMin =
                                    forecastList[index].location!!.precipitation!!._minvalue
                                mWeather.precMax =
                                    forecastList[index].location!!.precipitation!!._maxvalue
                            }
                            if (forecastList[index].location!!.symbol != null) {
                                mWeather.symbolId = forecastList[index].location!!.symbol!!._id
                                mWeather.symbolNumber =
                                    forecastList[index].location!!.symbol!!._number
                            }
                            langtid.add(mWeather)
                        }
                        sisteTil == klFraInt -> {
                            sisteTil = klTilInt
                            sisteDato = datoFra
                            val mWeather =
                                Weather()
                            mWeather.fraDato = datoFra
                            if (nyDag) {
                                mWeather.datoVise = datoFra
                                nyDag = false
                            }
                            mWeather.tilDato = datoTil
                            mWeather.fra = klFra
                            mWeather.til = klTil
                            if (forecastList[index].location!!.minTemperature != null) {
                                mWeather.minTemp =
                                    forecastList[index].location!!.minTemperature!!._value
                            }
                            if (forecastList[index].location!!.maxTemperature != null) {
                                mWeather.maxTemp =
                                    forecastList[index].location!!.maxTemperature!!._value
                            }
                            if (forecastList[index].location!!.precipitation != null) {
                                mWeather.precMin =
                                    forecastList[index].location!!.precipitation!!._minvalue
                                mWeather.precMax =
                                    forecastList[index].location!!.precipitation!!._maxvalue
                            }
                            if (forecastList[index].location!!.symbol != null) {
                                mWeather.symbolId = forecastList[index].location!!.symbol!!._id
                                mWeather.symbolNumber =
                                    forecastList[index].location!!.symbol!!._number
                            }
                            langtid.add(mWeather)
                        }
                        sisteDato != datoFra -> {
                            sisteTil = klTilInt
                            sisteDato = datoFra
                            val mWeather =
                                Weather()
                            mWeather.fraDato = datoFra
                            mWeather.tilDato = datoTil
                            mWeather.fra = klFra
                            mWeather.til = klTil
                            if (forecastList[index].location!!.minTemperature != null) {
                                mWeather.minTemp =
                                    forecastList[index].location!!.minTemperature!!._value
                            }
                            if (forecastList[index].location!!.maxTemperature != null) {
                                mWeather.maxTemp =
                                    forecastList[index].location!!.maxTemperature!!._value
                            }
                            if (forecastList[index].location!!.precipitation != null) {
                                mWeather.precMin =
                                    forecastList[index].location!!.precipitation!!._minvalue
                                mWeather.precMax =
                                    forecastList[index].location!!.precipitation!!._maxvalue
                            }
                            if (forecastList[index].location!!.symbol != null) {
                                mWeather.symbolId = forecastList[index].location!!.symbol!!._id
                                mWeather.symbolNumber =
                                    forecastList[index].location!!.symbol!!._number
                            }
                            langtid.add(mWeather)
                        }
                    }

                } else if (klTilInt == 0 && klFraInt != 0 && klFraInt != 23) {
                    sisteTil = klTilInt
                    sisteDato = datoFra
                    val mWeather =
                        Weather()
                    mWeather.fraDato = datoFra
                    mWeather.tilDato = datoTil
                    nyDag = true
                    mWeather.fra = klFra
                    mWeather.til = klTil
                    if (forecastList[index].location!!.minTemperature != null) {
                        mWeather.minTemp =
                            forecastList[index].location!!.minTemperature!!._value
                    }
                    if (forecastList[index].location!!.maxTemperature != null) {
                        mWeather.maxTemp =
                            forecastList[index].location!!.maxTemperature!!._value
                    }
                    if (forecastList[index].location!!.precipitation != null) {
                        mWeather.precMin =
                            forecastList[index].location!!.precipitation!!._minvalue
                        mWeather.precMax =
                            forecastList[index].location!!.precipitation!!._maxvalue
                    }
                    if (forecastList[index].location!!.symbol != null) {
                        mWeather.symbolId = forecastList[index].location!!.symbol!!._id
                        mWeather.symbolNumber =
                            forecastList[index].location!!.symbol!!._number
                    }
                    langtid.add(mWeather)
                }
            }
        }
    }
}
