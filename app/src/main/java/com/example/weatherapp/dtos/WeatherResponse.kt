package com.example.weatherapp.dtos

data class WeatherResponse(
    val location: Location,
    val current: Current,
    val forecast: Forecast
)
