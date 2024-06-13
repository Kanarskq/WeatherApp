package com.example.weatherapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavController
import com.example.weatherapp.R
import com.example.weatherapp.UserSession
import com.example.weatherapp.repositories.UserRepository
import com.example.weatherapp.ui.theme.BluePink
import com.example.weatherapp.viewModels.WelcomeViewModel
import com.example.weatherapp.viewModels.factories.WelcomeViewModelFactory

@Composable
fun WelcomeScreen(navController: NavController, userRepository: UserRepository) {
    val viewModel: WelcomeViewModel = viewModel(factory = WelcomeViewModelFactory(userRepository))
    val city = remember { mutableStateOf("Odesa") }

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
                    colors = listOf(Color.Transparent, Color.LightGray.copy(alpha = 0.1f)),
                    startY = 0.5f
                )
            )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to WeatherApp!",
            style = TextStyle(fontSize = 28.sp, color = Color(0xFFFFFBE5), letterSpacing = 1.sp),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Enter your city:",
            modifier = Modifier.padding(bottom = 8.dp),
            style = TextStyle(fontSize = 20.sp, color = Color(0xFFFFFBE5))
        )

        TextField(
            value = city.value,
            onValueChange = { city.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
            textStyle = TextStyle(color = Color(0xFFFFFBE5)),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xFFFFFBE5),
                backgroundColor = Color.Transparent,
                cursorColor = BluePink,
                focusedIndicatorColor = BluePink,
                unfocusedIndicatorColor = Color(0xFFFFFBE5).copy(alpha = 0.5f)
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (city.value.isNotEmpty()) {
                    val userEmail = UserSession.currentUserEmail
                    userEmail?.let {
                        viewModel.addCityToUserFavorites(it, city.value) {
                            navController.navigate("main/${city.value}")
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = BluePink),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Get Weather",
                style = TextStyle(fontSize = 18.sp, color = Color(0xFFFFFBE5))
            )
        }
    }
}

