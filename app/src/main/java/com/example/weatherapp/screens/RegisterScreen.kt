package com.example.weatherapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.weatherapp.dtos.User
import com.example.weatherapp.repositories.UserRepository

@Composable
fun RegisterScreen(navController: NavHostController, userRepository: UserRepository) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Register", style = TextStyle(fontSize = 24.sp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val newUser = User(id = 0, email = email, password = password)
                if (userRepository.addUser(newUser)) {
                    navController.navigate("login")
                } else {
                    errorMessage = "Email already exists"
                }
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "Register")
        }

        errorMessage?.let {
            Text(text = it, style = TextStyle(fontSize = 14.sp, color = Color.Red))
        }

        Button(
            onClick = {
                navController.navigate("login")
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "Back to Login")
        }
    }
}