package com.example.weatherapp.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.weatherapp.dtos.WeatherResponse
import com.example.weatherapp.instances.ApiClient
import com.example.weatherapp.services.WeatherApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _weatherResponse = mutableStateOf<WeatherResponse?>(null)
    val weatherResponse: State<WeatherResponse?> = _weatherResponse

    private val API_KEY = "cecff5365fa34a0fb3d191655240106"

    fun fetchWeather(city: String) {
        val apiService = ApiClient.instance.create(WeatherApiService::class.java)
        val call = apiService.getWeather(API_KEY, city)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    Log.d("Request Success", "Response: ${response.body()}")
                    _weatherResponse.value = weatherResponse
                } else {
                    Log.d("Request Error", "Server Error: ${response.code()}")
                    _weatherResponse.value = null
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("Request Error", "Request Error: ${t.message}")
                _weatherResponse.value = null
            }
        })
    }
}