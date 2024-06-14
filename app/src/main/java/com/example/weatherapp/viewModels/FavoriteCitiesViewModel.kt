package com.example.weatherapp.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.dtos.FavoriteCity
import com.example.weatherapp.dtos.ShortWeatherData
import com.example.weatherapp.dtos.User
import com.example.weatherapp.dtos.WeatherResponse
import com.example.weatherapp.instances.ApiClient
import com.example.weatherapp.repositories.UserRepository
import com.example.weatherapp.services.WeatherApiService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val API_KEY = "cecff5365fa34a0fb3d191655240106"

class FavoriteCitiesViewModel(
    private val userRepository: UserRepository,
    private val _user: User
) : ViewModel() {
    private val _favoriteCities = mutableStateListOf<FavoriteCity>()
    val favoriteCities: List<FavoriteCity>
        get() = _favoriteCities

    val user: User
        get() = _user

    private val _weatherData = mutableStateMapOf<String, ShortWeatherData>()
    val weatherData: Map<String, ShortWeatherData> get() = _weatherData

    init {
        _favoriteCities.addAll(_user.favoriteCities.map { FavoriteCity(it) })
    }

    fun isCityValid(cityName: String, callback: (Boolean) -> Unit) {
        val apiService = ApiClient.instance.create(WeatherApiService::class.java)
        val call = apiService.getWeather(API_KEY, cityName)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>, response: Response<WeatherResponse>
            ) {
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

    fun fetchWeatherForCity(cityName: String) {
        viewModelScope.launch {
            val apiService = ApiClient.instance.create(WeatherApiService::class.java)
            val call = apiService.getWeather(API_KEY, cityName)

            call.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>, response: Response<WeatherResponse>
                ) {
                    if (response.isSuccessful) {
                        val weatherApiResponse = response.body()
                        weatherApiResponse?.let {
                            _weatherData[cityName] = ShortWeatherData(
                                temp_c = it.current.temp_c,
                                temp_f = it.current.temp_f,
                                iconUrl = "https:" + it.current.condition.icon,
                                condition = it.current.condition.text
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {

                }
            })
        }
    }

    fun addFavoriteCity(cityName: String, callback: (Boolean) -> Unit) {
        isCityValid(cityName) { exists ->
            if (exists) {
                _favoriteCities.add(FavoriteCity(cityName))
                val updatedUser = _user.copy(favoriteCities = _favoriteCities.map { it.cityName })
                userRepository.updateUser(updatedUser)
                callback(true)
            } else {
                callback(false)
            }
        }
    }
    fun removeFavoriteCityAndUpdateUI(cityName: String) {
        removeFavoriteCity(cityName)
        _weatherData.remove(cityName)
    }

    fun removeFavoriteCity(cityName: String) {
        _favoriteCities.removeAll { it.cityName == cityName }
        val updatedUser = user.copy(favoriteCities = _favoriteCities.map { it.cityName })
        userRepository.updateUser(updatedUser)
    }
}