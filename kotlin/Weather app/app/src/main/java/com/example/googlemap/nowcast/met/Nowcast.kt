package com.example.googlemap.nowcast.met



data class Nowcast (val product: Product?, val meta: Meta?)

data class Product(val time: List<Time>?)

data class Time(val from: String?, val to: String?, val datatype: String?, val location: Location?)

data class Location(val longitude: String?, val latitude: String?, val precipitation: Precipitation?)

data class Precipitation(val value: String?, val unit: String?)

data class Meta(val model: Model?)

data class Model(val from: String?, val to: String?)