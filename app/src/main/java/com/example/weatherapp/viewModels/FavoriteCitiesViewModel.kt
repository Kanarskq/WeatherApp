package com.example.weatherapp.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.weatherapp.dtos.FavoriteCity
import com.example.weatherapp.dtos.WeatherResponse
import com.example.weatherapp.instances.ApiClient
import com.example.weatherapp.services.WeatherApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val API_KEY = "cecff5365fa34a0fb3d191655240106"
class FavoriteCitiesViewModel : ViewModel() {
    private val _favoriteCities = mutableStateListOf<FavoriteCity>()
    val favoriteCities: List<FavoriteCity>
        get() = _favoriteCities

    fun isCityValid(cityName: String, callback: (Boolean) -> Unit) {
        val apiService = ApiClient.instance.create(WeatherApiService::class.java)
        val call = apiService.getWeather(API_KEY, cityName)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherApiResponse = response.body()
                    val cityExists = weatherApiResponse?.error == null
                    callback(cityExists)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun addFavoriteCity(cityName: String) {
        _favoriteCities.add(FavoriteCity(cityName))
    }

    fun removeFavoriteCity(cityName: String) {
        _favoriteCities.removeAll { it.cityName == cityName }
    }
}