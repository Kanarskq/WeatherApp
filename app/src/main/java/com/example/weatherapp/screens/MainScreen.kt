package com.example.weatherapp.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.dtos.Condition
import com.example.weatherapp.dtos.Current
import com.example.weatherapp.dtos.Forecast
import com.example.weatherapp.dtos.Location
import com.example.weatherapp.dtos.WeatherResponse
import com.example.weatherapp.ui.theme.LightBluePink
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun MainCard(weather: WeatherResponse) {
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
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = weather.location.name,
                    style = TextStyle(fontSize = 24.sp)
                )
                Image(
                    painter = rememberAsyncImagePainter("https:${weather.current.condition.icon}"),
                    contentDescription = "Weather Icon",
                    modifier = Modifier
                        .padding(end = 8.dp, top = 8.dp)
                        .size(140.dp)
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "${weather.current.temp_c} °C",
                    style = TextStyle(fontSize = 24.sp)
                )

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
fun TabLayout(weather: WeatherResponse) {
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
                0 -> TabToday(weather)
                1 -> TabForecast(weather)
            }
        }
    }
}

@Composable
fun TabToday(weather: WeatherResponse) {
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
                Text(text = "${hour.temp_c} °C", style = TextStyle(fontSize = 18.sp))
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
fun TabForecast(weather: WeatherResponse) {
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
                Text(text = day.date, style = TextStyle(fontSize = 18.sp))
                Text(text = "${day.day.avgtemp_c} °C", style = TextStyle(fontSize = 18.sp))
                Image(
                    painter = rememberAsyncImagePainter("https:${day.day.condition.icon}"),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainCardPreview() {
    val sampleWeather = WeatherResponse(
        location = Location(
            name = "Odesa",
            region = "Odes'ka Oblast'",
            country = "Ukraine",
            localtime = "2024-06-11 16:36"
        ),
        current = Current(
            last_updated = "2024-06-11 16:30",
            temp_c = 23.5,
            temp_f = 74.3,
            condition = Condition(
                text = "Sunny",
                icon = "//cdn.weatherapi.com/weather/64x64/day/113.png",
                code = 1000
            ),
            wind_kph = 30.2,
            wind_dir = "S",
            humidity = 68,
            cloud = 5,
            feelslike_c = 25.3,
            feelslike_f = 77.5
        ),
        forecast = Forecast(
            forecastday = listOf()
        )
    )
    MainCard(weather = sampleWeather)
}