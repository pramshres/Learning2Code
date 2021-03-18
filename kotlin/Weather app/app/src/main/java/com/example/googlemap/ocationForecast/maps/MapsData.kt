package com.example.googlemap.ocationForecast.maps

data class MapsData(val html_attributions: List<String>,
                    val result: Result,
                    val status: String)

data class Result(val geometry: Geometry)

data class Geometry(val location: Location?)

data class Location(val lat: String, val lng: String)

