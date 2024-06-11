package com.example.weatherapp.dtos

data class Current(
    val last_updated: String,
    val temp_c: Double,
    val temp_f: Double,
    val condition: Condition,
    val wind_kph: Double,
    val wind_dir: String,
    val humidity: Int,
    val cloud: Int,
    val feelslike_c: Double,
    val feelslike_f: Double
)
