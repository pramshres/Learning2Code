package com.example.googlemap.ocationForecast.met

import com.google.gson.annotations.SerializedName

data class OceanForecast(@SerializedName("gml:id") val id: String?,
                         @SerializedName("mox:seaCurrentSpeed") val currentSpeed: SeaCurrentSpeed?,
                         @SerializedName("mox:validTime") val validTime: ValidTime?,
                         @SerializedName("mox:seaIcePresence") val seaIcePresence: SeaIcePresence?,
                         @SerializedName("mox:seaCurrentDirection") val seaCurrentDirection: SeaCurrentDirection?,
                         @SerializedName("mox:meanTotalWaveDirection") val meanTotalWaveDirection: MeanTotalWaveDirection?,
                         @SerializedName("mox:seaTemperature") val seaTemperature: SeaTemperature?,
                         @SerializedName("mox:seaBottomTopography") val seaBottomTopography: SeaBottomTopography?,
                         @SerializedName("mox:significantTotalWaveHeight") val significantTotalWaveHeight: SignificantTotalWaveHeight?)


data class SeaCurrentSpeed(val content: String?, val uom : String?)

data class ValidTime(@SerializedName("gml:TimePeriod") val timePeriod: TimePeriod?)

data class TimePeriod(@SerializedName("gml:id") val id: String?,
                      @SerializedName("gml:end") val end: String?,
                      @SerializedName("gml:begin") val begin: String?)

data class SeaIcePresence(val content: String?, val uom : String?)

data class SeaCurrentDirection(val content: String?, val uom : String?)

data class MeanTotalWaveDirection(val content: String?, val uom : String?)

data class SeaTemperature(val content: String?, val uom : String?)

data class SeaBottomTopography(val uom : String?, val content: String?)

data class SignificantTotalWaveHeight(val content: String?, val uom : String?)
