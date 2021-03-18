package com.example.googlemap.ocationForecast.nearby

import com.google.android.gms.maps.model.LatLng

class Sted(val pos: LatLng) {
    var ikonId = ""
    var temp = ""
    var weather = ""
    var vindRetning = ""
    var vindStyrke = ""

    fun hentVindRetning(): String{
        return when(vindRetning){
            "S" -> "Sør"
            "SE" -> "Sørøst"
            "SW" -> "Sørvest"
            "N" -> "Nord"
            "NE" -> "Nordøst"
            "NW" -> "Nordvest"
            "E" -> "Øst"
            "W" -> "Vest"
            else -> ""
        }
    }
}