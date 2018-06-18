package br.com.mapsmarker.model.domain

class LatLngBO(
        var latitude: Double,
        var longitude: Double
)

const val LAT_MAX_VALUE = 180.0
const val LAT_MIN_VALUE = -180.0
const val LNG_MAX_VALUE = 90.0
const val LNG_MIN_VALUE = -90.0
