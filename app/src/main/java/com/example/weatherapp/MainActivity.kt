package com.example.weatherapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.weatherapp.dtos.WeatherResponse
import com.example.weatherapp.instances.ApiClient
import com.example.weatherapp.screens.MainCard
import com.example.weatherapp.screens.TabLayout
import com.example.weatherapp.services.WeatherApiService
import com.example.weatherapp.ui.theme.WeatherAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val API_KEY = "cecff5365fa34a0fb3d191655240106"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWeatherResponse("Odesa", this) { weatherResponse ->
            setContent {
                WeatherAppTheme {
                    weatherResponse?.let {
                        Image(
                            painter = painterResource(id = R.drawable.main_background),
                            contentDescription = "background",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        Column{
                            MainCard(it)
                            TabLayout(it)
                        }
                    }
                }
            }
        }
    }
}

private fun getWeatherResponse (city: String, context: Context, callback: (WeatherResponse?) -> Unit){
    val apiService = ApiClient.instance.create(WeatherApiService::class.java)
    val call = apiService.getWeather(API_KEY, city)

    call.enqueue(object : Callback<WeatherResponse> {
        override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
            if (response.isSuccessful) {
                val weatherResponse = response.body()
                Log.d("Request Success", "Response: ${response.body()}")
                callback(weatherResponse)
            } else {
                Log.d("Request Error", "Error: ${response.code()}")
                callback(null)
            }
        }

        override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
            Log.d("Request Error", "VolleyError: ${t.message}")
            callback(null)
        }
    })
}
