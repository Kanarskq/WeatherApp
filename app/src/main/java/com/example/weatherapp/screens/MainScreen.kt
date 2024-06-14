package com.example.weatherapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.weatherapp.ui.theme.BluePink
import com.example.weatherapp.ui.theme.LightBluePink
import com.example.weatherapp.viewModels.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    val weatherResponse by mainViewModel.weatherResponse

    LaunchedEffect(Unit) {
        mainViewModel.fetchWeather()
    }

    Image(
        painter = painterResource(id = R.drawable.main_background),
        contentDescription = "background",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Gray.copy(alpha = 0.1f)),
                    startY = 0.5f
                )
            )
    )
    weatherResponse?.let { weather ->
        Column {
            MainCard(weather, mainViewModel.user, onSettingsClick = {
                navController.navigate("settings")
            }, onFavoriteCitiesClick = {
                navController.navigate("favorite_cities")
            })
            TabLayout(weather, mainViewModel.user)
        }
    }?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Loading...")
        }
    }
}

@Composable
fun MainCard(
    weather: WeatherResponse,
    user: User,
    onSettingsClick: () -> Unit,
    onFavoriteCitiesClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = LightBluePink.copy(alpha = 0.3f),
            elevation = 0.dp,
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 4.dp, start = 8.dp),
                        text = weather.location.localtime.split(" ")[0],
                        style = TextStyle(fontSize = 24.sp, color = Color(0xFFFFFBE5))
                    )

                    Text(
                        modifier = Modifier
                            .padding(top = 4.dp, end = 8.dp),
                        text = weather.location.localtime.split(" ")[1],
                        style = TextStyle(fontSize = 24.sp, color = Color(0xFFFFFBE5))
                    )
                }
                Row(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(

                        text = weather.location.name,
                        style = TextStyle(fontSize = 32.sp, color = Color(0xFFFFFBE5))
                    )
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite Icon",
                        tint = Color(0xFFFFFBE5),
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
                        style = TextStyle(fontSize = 24.sp, color = Color(0xFFFFFBE5))
                    )
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings Icon",
                        tint = Color(0xFFFFFBE5),
                        modifier = Modifier
                            .padding(8.dp)
                            .size(20.dp)
                            .clickable { onSettingsClick() },
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                    text = weather.current.condition.text,
                    style = TextStyle(fontSize = 18.sp, color = Color(0xFFFFFBE5))
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.wind_speed_icon), // Add your wind speed icon here
                            contentDescription = "Wind Speed",
                            tint = Color(0xFFFFFBE5),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "${weather.current.wind_kph} kph",
                            style = TextStyle(fontSize = 18.sp, color = Color(0xFFFFFBE5))
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.wind_direction_icon), // Add your wind direction icon here
                            contentDescription = "Wind Direction",
                            tint = Color(0xFFFFFBE5),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = weather.current.wind_dir,
                            style = TextStyle(fontSize = 18.sp, color = Color(0xFFFFFBE5))
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.humidity_icon), // Add your humidity icon here
                            contentDescription = "Humidity",
                            tint = Color(0xFFFFFBE5),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "${weather.current.humidity}%",
                            style = TextStyle(fontSize = 18.sp, color = Color(0xFFFFFBE5))
                        )
                    }
                }
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
            .padding(start = 4.dp, end = 4.dp)
            .clip(RoundedCornerShape(10.dp))
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
                            style = TextStyle(fontSize = 18.sp, color = Color(0xFFFFFBE5))
                        )
                    },
                    selectedContentColor = BluePink,
                    unselectedContentColor = BluePink.copy(alpha = 0.6f)
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                backgroundColor = LightBluePink.copy(alpha = 0.3f),
                elevation = 0.dp,
                shape = RoundedCornerShape(10.dp)
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = time,
                        style = TextStyle(fontSize = 18.sp, color = Color(0xFFFFFBE5)),
                        color = Color(0xFFFFFBE5)
                    )
                    Text(
                        text = if (user.tempUnit == "celsius") "${hour.temp_c} °C" else "${hour.temp_f} °F",
                        style = TextStyle(fontSize = 18.sp), color = Color(0xFFFFFBE5)
                    )
                    Image(
                        painter = rememberAsyncImagePainter("https:${hour.condition.icon}"),
                        contentDescription = "Weather Icon",
                        modifier = Modifier.size(48.dp)
                    )
                }
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                backgroundColor = LightBluePink.copy(alpha = 0.3f),
                elevation = 0.dp,
                shape = RoundedCornerShape(10.dp)
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = day.date,
                        style = TextStyle(fontSize = 18.sp, color = Color(0xFFFFFBE5)),
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = if (user.tempUnit == "celsius") "${day.day.avgtemp_c} °C" else "${day.day.avgtemp_f} °F",
                        style = TextStyle(fontSize = 18.sp, color = Color(0xFFFFFBE5)),
                        modifier = Modifier.padding(8.dp)
                    )
                    Image(
                        painter = rememberAsyncImagePainter("https:${day.day.condition.icon}"),
                        contentDescription = "Weather Icon",
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}