package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.screens.MainScreen
import com.example.weatherapp.screens.SettingsScreen
import com.example.weatherapp.screens.WelcomeScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }
}

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(navController = navController)
        }
        composable("main/{city}/{tempUnit}") { backStackEntry ->
            val city = backStackEntry.arguments?.getString("city") ?: "Odesa"
            val tempUnit = backStackEntry.arguments?.getString("tempUnit") ?: "celsius"
            MainScreen(city = city, navController, tempUnit = tempUnit)
        }
        composable("settings/{city}") { backStackEntry ->
            val city = backStackEntry.arguments?.getString("city") ?: "Odesa"
            SettingsScreen(navController = navController, city = city)
        }
    }
}
