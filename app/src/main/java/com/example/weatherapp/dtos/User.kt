package com.example.weatherapp.dtos


data class User(
    val id: Int,
    val email: String,
    val password: String,
    val tempUnit: String = "celsius",
    var favoriteCities: List<String> = emptyList()
)

