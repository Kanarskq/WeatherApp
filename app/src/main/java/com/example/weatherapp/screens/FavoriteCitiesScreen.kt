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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.weatherapp.dtos.FavoriteCity
import com.example.weatherapp.dtos.ShortWeatherData
import com.example.weatherapp.dtos.User
import com.example.weatherapp.repositories.UserRepository
import com.example.weatherapp.ui.theme.BluePink
import com.example.weatherapp.ui.theme.LightBluePink
import com.example.weatherapp.viewModels.FavoriteCitiesViewModel
import java.util.Locale

@Composable
fun FavoriteCitiesScreen(
    favoriteCitiesViewModel: FavoriteCitiesViewModel,
    navController: NavHostController,
    user: User,
    userRepository: UserRepository
) {
    var newFavoriteCity by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf(false) }
    var favoriteCities by remember { mutableStateOf(user.favoriteCities) }

    LaunchedEffect(favoriteCities) {
        favoriteCities.forEach { city ->
            favoriteCitiesViewModel.fetchWeatherForCity(city)
        }
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
                    colors = listOf(Color.Transparent, Color.Gray.copy(alpha = 0.1f)), startY = 0.5f
                )
            )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Favorite Places", style = TextStyle(
                fontSize = 24.sp, color = Color(0xFFFFFBE5)
            ), modifier = Modifier.padding(vertical = 16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(value = newFavoriteCity,
                onValueChange = { newFavoriteCity = it },
                label = { Text("Enter city name", color = Color(0xFFFFFBE5)) },
                modifier = Modifier
                    .weight(0.8f)
                    .padding(end = 8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFFFFBE5),
                    unfocusedBorderColor = Color(0xFFFFFBE5).copy(alpha = 0.5f),
                    textColor = Color(0xFFFFFBE5)
                )
            )

            Button(
                onClick = {
                    if (newFavoriteCity.isNotBlank()) {
                        favoriteCitiesViewModel.isCityValid(newFavoriteCity) { exists ->
                            if (exists) {
                                val updatedUser =
                                    user.copy(favoriteCities = user.favoriteCities + newFavoriteCity)
                                userRepository.updateUser(updatedUser)
                                favoriteCities = updatedUser.favoriteCities
                                newFavoriteCity = ""
                                showMessage = false
                            } else {
                                showMessage = true
                            }
                        }
                    }
                },
                modifier = Modifier
                    .weight(0.2f)
                    .heightIn(min = 64.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = BluePink)
            ) {
                Text(text = "+", style = TextStyle(fontSize = 24.sp, color = Color(0xFFFFFBE5)))
            }
        }

        if (showMessage) {
            Text(
                text = "Unable to find the city '$newFavoriteCity'",
                style = TextStyle(fontSize = 14.sp, color = Color.Red),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        LazyColumn {
            items(favoriteCities) { favoriteCity ->
                val weatherData = favoriteCitiesViewModel.weatherData[favoriteCity]
                FavoriteCityItem(favoriteCity = FavoriteCity(favoriteCity),
                    weatherData = weatherData,
                    user = user,
                    onItemClick = {
                        navController.navigate("main/$favoriteCity")
                    },
                    onDeleteItemClick = {
                        val updatedFavoriteCities =
                            user.favoriteCities.filter { it != favoriteCity }
                        val updatedUser = user.copy(favoriteCities = updatedFavoriteCities)
                        userRepository.updateUser(updatedUser)
                        favoriteCities = updatedUser.favoriteCities
                        favoriteCitiesViewModel.removeFavoriteCity(favoriteCity)
                    })
            }
        }
    }
}

@Composable
fun FavoriteCityItem(
    favoriteCity: FavoriteCity,
    weatherData: ShortWeatherData?,
    user: User,
    onItemClick: () -> Unit,
    onDeleteItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .clickable(onClick = onItemClick),
        backgroundColor = LightBluePink.copy(alpha = 0.3f),
        elevation = 0.dp,
        shape = RoundedCornerShape(10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = favoriteCity.cityName.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                },
                style = TextStyle(fontSize = 18.sp, color = Color(0xFFFFFBE5)),
                modifier = Modifier.weight(1f)
            )
            weatherData?.let {
                Text(
                    text = if (user.tempUnit == "celsius") "${weatherData.temp_c} °C" else "${weatherData.temp_f} °F",
                    style = TextStyle(fontSize = 18.sp, color = Color(0xFFFFFBE5)),
                )
                Image(
                    painter = rememberAsyncImagePainter(it.iconUrl),
                    contentDescription = it.condition,
                    modifier = Modifier.size(48.dp)
                )
            }

            IconButton(
                onClick = onDeleteItemClick
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFFFFBE5))
            }
        }
    }
}
