package com.example.weatherapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.BluePink
import com.example.weatherapp.viewModels.RegisterViewModel

@Composable
fun RegisterScreen(navController: NavHostController, registerViewModel: RegisterViewModel) {
    val email by registerViewModel.email
    val password by registerViewModel.password
    val errorMessage by registerViewModel.errorMessage

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
            .padding(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Register",
            style = TextStyle(fontSize = 24.sp, color = Color(0xFFFFFBE5)),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { registerViewModel.email.value = it },
            label = { Text("Email", color = Color(0xFFFFFBE5)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = BluePink,
                unfocusedBorderColor = Color(0xFFFFFBE5).copy(alpha = 0.5f),
                textColor = Color(0xFFFFFBE5)
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = { registerViewModel.password.value = it },
            label = { Text("Password", color = Color(0xFFFFFBE5)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = BluePink,
                unfocusedBorderColor = Color(0xFFFFFBE5).copy(alpha = 0.5f),
                textColor = Color(0xFFFFFBE5)
            )
        )
        Button(
            onClick = {
                registerViewModel.registerUser {
                    navController.navigate("login")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = BluePink),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Register",
                style = TextStyle(fontSize = 18.sp, color = Color(0xFFFFFBE5))
            )
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                style = TextStyle(fontSize = 18.sp, color = Color.Red)
            )
        }

        Button(
            onClick = {
                navController.navigate("login")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = BluePink),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Back to Login",
                style = TextStyle(fontSize = 18.sp, color = Color(0xFFFFFBE5))
            )
        }
    }
}