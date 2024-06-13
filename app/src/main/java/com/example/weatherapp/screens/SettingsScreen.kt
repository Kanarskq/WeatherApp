package com.example.weatherapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.weatherapp.R
import com.example.weatherapp.repositories.UserRepository
import com.example.weatherapp.ui.theme.BluePink
import com.example.weatherapp.ui.theme.LightBluePink
import com.example.weatherapp.viewModels.SettingsViewModel
import com.example.weatherapp.viewModels.factories.SettingsViewModelFactory

@Composable
fun SettingsScreen(navController: NavHostController, userEmail: String, userRepository: UserRepository) {
    val viewModel: SettingsViewModel = viewModel(factory = SettingsViewModelFactory(userRepository, userEmail))
    val user by viewModel.user.collectAsState()

    user?.let { currentUser ->
        var isCelsius by remember { mutableStateOf(currentUser.tempUnit == "celsius") }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Settings", style = TextStyle(fontSize = 32.sp, color = Color(0xFFFFFBE5)))
            Spacer(modifier = Modifier.height(32.dp))
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
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Temperature Unit: ",
                        style = TextStyle(fontSize = 18.sp, color = Color(0xFFFFFBE5))
                    )

                    RadioButton(
                        selected = isCelsius,
                        onClick = { isCelsius = true },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = LightBluePink,
                            unselectedColor = Color.Gray
                        )
                    )
                    Text(
                        text = "Celsius",
                        style = TextStyle(fontSize = 18.sp, color = Color(0xFFFFFBE5))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    RadioButton(
                        selected = !isCelsius,
                        onClick = { isCelsius = false },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = LightBluePink,
                            unselectedColor = Color.Gray
                        )
                    )
                    Text(
                        text = "Fahrenheit",
                        style = TextStyle(fontSize = 18.sp, color = Color(0xFFFFFBE5))
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    onClick = {
                        viewModel.updateUserTemperatureUnit(isCelsius)
                        navController.navigate("main/${currentUser.favoriteCities.firstOrNull()}")
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = BluePink),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Save and Return",
                        style = TextStyle(fontSize = 18.sp, color = Color(0xFFFFFBE5))
                    )
                }
            }
        }
    }
}