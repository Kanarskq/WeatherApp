package com.example.weatherapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.R
import com.example.weatherapp.dtos.WeatherResponse
import com.example.weatherapp.instances.RetrofitInstance
import com.example.weatherapp.ui.theme.LightBluePink

@Preview(showBackground = true)
@Composable
fun MainScreen() {
    var weatherResponse: WeatherResponse? = null

    LaunchedEffect(Unit) {
        val response = RetrofitInstance.api.getWeatherForecast(
            lat = 37.7749, // Example latitude for San Francisco
            lon = -122.4194, // Example longitude for San Francisco
            apiKey = "e145904ddc317862ff19e7b6afbad606"
        )
        weatherResponse = response
    }

    Image(
        painter = painterResource(id = R.drawable.main_background),
        contentDescription = "background",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.3f), colors = CardDefaults.cardColors(
                containerColor = LightBluePink

            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp,
            ),
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
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                        text = "1 Jun 2024 12:00",
                        style = TextStyle(fontSize = 24.sp)
                    )

                    weatherResponse?.let {
                        Image(
                            painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/${it.list[0].weather[0].icon}.png"),
                            contentDescription = "Weather Icon",
                            modifier = Modifier
                                .padding(end = 8.dp, top = 8.dp)
                                .size(48.dp)
                        )
                    }
                }
                weatherResponse?.let {
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = it.list[0].weather[0].main,
                        style = TextStyle(fontSize = 24.sp)
                    )

                    Text(
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                        text = it.city.name,
                        style = TextStyle(fontSize = 18.sp)
                    )
                }
            }

        }
    }
}