package com.example.weatherapp.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.R
import com.example.weatherapp.dtos.User
import com.example.weatherapp.dtos.WeatherResponse
import com.example.weatherapp.instances.ApiClient
import com.example.weatherapp.services.WeatherApiService
import com.example.weatherapp.ui.theme.LightBluePink
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun MainScreen(city: String, navController: NavHostController, user: User) {
    var weatherResponse by remember { mutableStateOf<WeatherResponse?>(null) }

    LaunchedEffect(city) {
        getWeatherResponse(city) { response ->
            weatherResponse = response
        }
    }

    weatherResponse?.let { weather ->
        Image(
            painter = painterResource(id = R.drawable.main_background),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column {
            MainCard(weather, user, onSettingsClick = {
                navController.navigate("settings")
            }, onFavoriteCitiesClick = {
                navController.navigate("favorite_cities")
            })
            TabLayout(weather, user)
        }
    }
}

private const val API_KEY = "cecff5365fa34a0fb3d191655240106"
private fun getWeatherResponse(city: String, callback: (WeatherResponse?) -> Unit) {
    val apiService = ApiClient.instance.create(WeatherApiService::class.java)
    val call = apiService.getWeather(API_KEY, city)

    call.enqueue(object : Callback<WeatherResponse> {
        override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
            if (response.isSuccessful) {
                val weatherResponse = response.body()
                Log.d("Request Success", "Response: ${response.body()}")
                callback(weatherResponse)
            } else {
                Log.d("Request Error", "Server Error: ${response.code()}")
                callback(null)
            }
        }

        override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
            Log.d("Request Error", "Request Error: ${t.message}")
            callback(null)
        }
    })
}

@Composable
fun MainCard(weather: WeatherResponse, user: User, onSettingsClick: () -> Unit, onFavoriteCitiesClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(5.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = LightBluePink.copy(alpha = 0.3f),
            elevation = 0.dp,
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp, start = 8.dp),
                        text = weather.location.localtime.split(" ")[0],
                        style = TextStyle(fontSize = 24.sp)
                    )

                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp, end = 8.dp),
                        text = weather.location.localtime.split(" ")[1],
                        style = TextStyle(fontSize = 24.sp)
                    )
                }
                Row {
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = weather.location.name,
                        style = TextStyle(fontSize = 24.sp)
                    )
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite Icon",
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(20.dp)
                            .clickable { onFavoriteCitiesClick() }
                    )
                }

                Image(
                    painter = rememberAsyncImagePainter("https:${weather.current.condition.icon}"),
                    contentDescription = "Weather Icon",
                    modifier = Modifier
                        .padding(end = 8.dp, top = 8.dp)
                        .size(140.dp)
                )
                Row {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = if (user.tempUnit == "celsius") "${weather.current.temp_c} °C" else "${weather.current.temp_f} °F",
                        style = TextStyle(fontSize = 24.sp)
                    )
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings Icon",
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(20.dp)
                            .clickable { onSettingsClick() }
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                    text = weather.current.condition.text,
                    style = TextStyle(fontSize = 18.sp)
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayout(weather: WeatherResponse, user: User) {
    val tabList = listOf("Today", "Forecast")
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp)
            .clip(RoundedCornerShape(5.dp))
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = LightBluePink.copy(alpha = 0.3f)
        ) {
            tabList.forEachIndexed { index, text ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                            text = text,
                            style = TextStyle(fontSize = 18.sp)
                        )
                    }
                )
            }
        }
        HorizontalPager(
            count = tabList.size,
            state = pagerState,
            modifier = Modifier.weight(1.0f)
        ) { page ->
            when (page) {
                0 -> TabToday(weather, user)
                1 -> TabForecast(weather, user)
            }
        }
    }
}

@Composable
fun TabToday(weather: WeatherResponse, user: User) {
    LazyColumn {
        items(weather.forecast.forecastday.first().hour) { hour ->
            val time = hour.time.split(" ")[1]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = time, style = TextStyle(fontSize = 18.sp))
                Text(
                    text = if (user.tempUnit == "celsius") "${hour.temp_c} °C" else "${hour.temp_f} °F",
                    style = TextStyle(fontSize = 18.sp))
                Image(
                    painter = rememberAsyncImagePainter("https:${hour.condition.icon}"),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Composable
fun TabForecast(weather: WeatherResponse, user: User) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(weather.forecast.forecastday) { day ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = day.date,
                    style = TextStyle(fontSize = 18.sp),
                    modifier = Modifier.padding(8.dp))
                Text(text = if (user.tempUnit == "celsius") "${day.day.avgtemp_c} °C" else "${day.day.avgtemp_f} °F",
                    style = TextStyle(fontSize = 18.sp),
                    modifier = Modifier.padding(8.dp))
                Image(
                    painter = rememberAsyncImagePainter("https:${day.day.condition.icon}"),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}