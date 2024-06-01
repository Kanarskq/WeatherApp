package com.example.weatherapp.dtos

data class WeatherResponse(
    val list: List<WeatherData>,
    val city: City
)
