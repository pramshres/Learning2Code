package com.example.googlemap.ocationForecast.locationForecast

import com.google.gson.annotations.SerializedName

data class LocForData(@SerializedName("product") val product: Product?)

data class Product(@SerializedName("time") val time: List<Time>?)

data class Time(@SerializedName("location") val location: Location?,
                @SerializedName("from") val _from: String,
                @SerializedName("to") val _to: String) //en og en time

data class Location(@SerializedName("temperature") var temperature: Temperature?,
                    @SerializedName("windDirection") var windDirection: WindDirection?,
                    @SerializedName("windSpeed") var windSpeed: WindSpeed?,
                    @SerializedName("cloudiness") val cloudiness: Cloudiness?,
                    @SerializedName("fog") val fog: Fog?,
                    //partall
                    @SerializedName("precipitation") var precipitation: Precipitation?,
                    @SerializedName("symbol") var symbol: Symbol?,

                    @SerializedName("altitude") val _altitude: String?,
                    @SerializedName("latitude") val _latitude: String?,
                    @SerializedName("longitude") val _longitude: String?,
                    @SerializedName("minTemperature") val minTemperature: MinTemperature?,
                    @SerializedName("maxTemperature") val maxTemperature: MaxTemperature?)



//here stops the classes
data class Temperature(@SerializedName("id") val _id: String,
                       @SerializedName("unit") val _unit:String,
                       @SerializedName("value") val _value: String)

data class MinTemperature(@SerializedName("id") val _id: String,
                       @SerializedName("unit") val _unit:String,
                       @SerializedName("value") val _value: String)

data class MaxTemperature(@SerializedName("id") val _id: String,
                       @SerializedName("unit") val _unit:String,
                       @SerializedName("value") val _value: String)

data class WindDirection(@SerializedName("id") val _id: String,
                         @SerializedName("deg") val _deg: String,
                         @SerializedName("name") val _name: String)

data class WindSpeed(@SerializedName("id") val _id: String,
                     @SerializedName("mps") val _mps: String,
                     @SerializedName("beaufort") val _beaufort: String,
                     @SerializedName("name") val _name: String)

data class Cloudiness(@SerializedName("id") val _id: String,
                      @SerializedName("percent") val _percent: String)

data class Fog(@SerializedName("id") val _id: String,
               @SerializedName("percent") val _percent: String)


data class Precipitation(@SerializedName("unit") val _unit: String,
                         @SerializedName("value") val _value: String,
                         @SerializedName("minvalue") val _minvalue: String,
                         @SerializedName("maxvalue") val _maxvalue: String)

data class Symbol(@SerializedName("id") val _id: String,
                  @SerializedName("number") val _number: String)


