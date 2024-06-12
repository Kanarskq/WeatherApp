package com.example.weatherapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.weatherapp.dtos.FavoriteCity
import com.example.weatherapp.viewModels.FavoriteCitiesViewModel

@Composable
fun FavoriteCitiesScreen(
    favoriteCitiesViewModel: FavoriteCitiesViewModel,
    navController: NavHostController
) {
    var newFavoriteCity by rememberSaveable { mutableStateOf("") }
    var showMessage by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        // Text for the title
        Text(
            text = "Favorite Places",
            style = TextStyle(fontSize = 24.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = newFavoriteCity,
            onValueChange = { newFavoriteCity = it },
            label = { Text("Enter city name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Button for adding a new favorite city
        Button(
            onClick = {
                if (newFavoriteCity.isNotBlank()) {
                    favoriteCitiesViewModel.isCityValid(newFavoriteCity) { exists ->
                        if (exists) {
                            favoriteCitiesViewModel.addFavoriteCity(newFavoriteCity)
                            newFavoriteCity = ""
                            showMessage = false
                        } else {
                            showMessage = true
                        }
                    }
                }
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "Add Favorite Place")
        }

        if (showMessage) {
            Text(
                text = "Unable to find the city '$newFavoriteCity'",
                style = TextStyle(fontSize = 14.sp, color = Color.Red),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        LazyColumn {
            items(favoriteCitiesViewModel.favoriteCities) { favoriteCity ->
                FavoriteCityItem(
                    favoriteCity = favoriteCity,
                    onItemClick = {
                        navController.navigate("main/${favoriteCity.cityName}/celsius")
                    }
                )
            }
        }
    }
}

@Composable
fun FavoriteCityItem(
    favoriteCity: FavoriteCity,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = favoriteCity.cityName,
            style = TextStyle(fontSize = 18.sp),
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = onItemClick
        ) {
            Icon(Icons.Default.Info, contentDescription = "Open")
        }
    }
}