package com.example.weatherapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun SettingsScreen(navController: NavHostController, city: String) {
    var isCelsius by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Settings", style = TextStyle(fontSize = 24.sp))

        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Temperature Unit: ", style = TextStyle(fontSize = 18.sp))

            RadioButton(
                selected = isCelsius,
                onClick = { isCelsius = true }
            )
            Text(text = "Celsius", style = TextStyle(fontSize = 18.sp))

            RadioButton(
                selected = !isCelsius,
                onClick = { isCelsius = false }
            )
            Text(text = "Fahrenheit", style = TextStyle(fontSize = 18.sp))
        }

        Button(
            onClick = { navController.navigate("main/$city/${if (isCelsius) "celsius" else "fahrenheit"}") }
        ) {
            Text(text = "Save and Return", style = TextStyle(fontSize = 18.sp))
        }
    }
}