package com.example.googlemap.ocationForecast.maps

data class AddressData(val results: List<Resultat>?, val plus_code: PlusCode?)

data class Resultat(val formatted_address: String?)
data class PlusCode(val compound_code: String?, val global_code: String?)

