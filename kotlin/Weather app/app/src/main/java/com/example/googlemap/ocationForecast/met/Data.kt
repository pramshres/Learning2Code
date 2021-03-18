package com.example.googlemap.ocationForecast.met

import com.google.gson.annotations.SerializedName

data class Data(@SerializedName("product") val issueTime: Product?,
                @SerializedName("meta") val nextIssueTime: NextIssueTime?,
                @SerializedName("mox:forecast") val forecast: List<Forecast>?)

data class Forecast(@SerializedName("metno:OceanForecast") val oceanForecast: OceanForecast?)

data class Product(@SerializedName("time") val timeInstant: TimeInstant?)

data class NextIssueTime(@SerializedName("gml:TimeInstant") val timeInstant: TimeInstant?)

data class TimeInstant(@SerializedName("gml:id") val id: String?, @SerializedName("gml:timePosition") val timePosition: String?)
