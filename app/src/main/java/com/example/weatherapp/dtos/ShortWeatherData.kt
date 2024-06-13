package com.example.weatherapp.dtos

data class ShortWeatherData (
    val temp_c: Double,
    val temp_f: Double,
    val iconUrl: String,
    val condition: String
)
