package com.example.googlemap.ocationForecast.nearby


data class NearbyData(val results: List<Result>?)

data class Result(val geometry: Geometry?,
                  val icon: String?,
                  val id: String?,
                  val name: String?,
                  val photos: List<Photo>?,
                  val place_id: String?,
                  val rating: String?,
                  val user_ratings_total: String?,
                  val vicinity: String?,
                  val types: List<String>?)

data class Geometry(val location: Location?)

data class Photo(val height: String?,
                 val width: String?,
                 val photo_reference: String?)


data class Location(val lat: String?, val lng: String?)
