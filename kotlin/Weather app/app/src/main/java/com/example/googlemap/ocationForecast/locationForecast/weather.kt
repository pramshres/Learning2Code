package com.example.googlemap.ocationForecast.locationForecast

class Weather {
    var dato=""
    var kl=""
    var temperatureUnit=""
    var temperatureValue=""
    var windDirectionDeg=""
    var windDirectionName=""
    var windSpeedMps=""
    var windSpeedBeaufort=""
    var windSpeedName=""
    var precipitationValue=""
    var precipitationUnit=""
    var symbolId=""
    var symbolNumber=""

    var fra=""
    var fraDato=""
    var til=""
    var tilDato=""
    var datoVise=""

    var minTemp=""
    var maxTemp=""
    var precMin=""
    var precMax=""


    fun getIkon():String{
        val baseURL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/weathericon/1.1"
        val product = "?symbol=$symbolNumber&content_type=image/svg%2Bxml"
        return "$baseURL$product"
    }


    fun getPenDato():String{
        var aar = ""
        var mnd = ""
        var dag = ""
        var teller = 0
        for (char in dato){
            if (char == '-'){
                teller++
            }
            else {
                when (teller) {
                    0 -> {
                        aar += char
                    }
                    1 -> {
                        mnd += char
                    }
                    else -> {
                        dag += char
                    }
                }
            }
        }
        val nyDato = "$dag/$mnd $aar"
        return "$nyDato kl. $kl:00"
    }


    fun visDato():String{
        if (datoVise == ""){
            return ""
        }
        else {
            var aar = ""
            var mnd = ""
            var dag = ""
            var teller = 0
            for (char in datoVise){
                if (char == '-'){
                    teller++
                }
                else {
                    when (teller) {
                        0 -> {
                            aar += char
                        }
                        1 -> {
                            mnd += char
                        }
                        else -> {
                            dag += char
                        }
                    }
                }
            }
            return "$dag/$mnd $aar"
        }
    }


    fun getVindRetning():String{
        var forste = ""
        var teller = 0
        for (char in windDirectionName){
            when (teller) {
                0 -> {
                    when (char) {
                        'N' -> {
                            forste = "Nord"
                            teller++
                        }
                        'S' -> {
                            forste = "Sør"
                            teller++
                        }
                        'E' -> {
                            forste = "Øst"
                            teller++
                        }
                        'W' -> {
                            forste = "Vest"
                            teller++
                        }
                    }
                }
                1 -> {
                    forste +="-"
                    when (char) {
                        'N' -> {
                            forste += "Nord"
                            teller++
                        }
                        'S' -> {
                            forste += "Sør"
                            teller++
                        }
                        'E' -> {
                            forste += "Øst"
                            teller++
                        }
                        'W' -> {
                            forste += "Vest"
                            teller++
                        }
                    }
                }
                else -> {
                    forste += "+//TODO"
                }
            }
        }
        return forste
    }


}