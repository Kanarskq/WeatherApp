package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.repositories.UserRepository
import com.example.weatherapp.screens.FavoriteCitiesScreen
import com.example.weatherapp.screens.LoginScreen
import com.example.weatherapp.screens.MainScreen
import com.example.weatherapp.screens.RegisterScreen
import com.example.weatherapp.screens.SettingsScreen
import com.example.weatherapp.screens.WelcomeScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewModels.FavoriteCitiesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userRepository = UserRepository(applicationContext)
        setContent {
            WeatherAppTheme {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController, userRepository = userRepository)
            }
        }
    }
}

object UserSession {
    var currentUserEmail: String? = null
}

@Composable
fun SetupNavGraph(navController: NavHostController, userRepository: UserRepository) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController, userRepository = userRepository)
        }
        composable("register") {
            RegisterScreen(navController = navController, userRepository = userRepository)
        }
        composable("welcome") {
            WelcomeScreen(navController = navController, userRepository = userRepository)
        }
        composable("main/{city}") { backStackEntry ->
            val city = backStackEntry.arguments?.getString("city") ?: "Odesa"
            val userEmail = UserSession.currentUserEmail
            userEmail?.let { email ->
                val user = userRepository.getUserByEmail(email)
                user?.let { user ->
                    MainScreen(city = city, navController = navController, user = user)
                }
            }
        }
        composable("settings") { backStackEntry ->
            val userEmail = UserSession.currentUserEmail
            userEmail?.let {
                val user = userRepository.getUserByEmail(it)
                user?.let {
                    SettingsScreen(navController = navController, user = it, userRepository = userRepository)
                }
            }
        }
        composable("favorite_cities") {
            val userEmail = UserSession.currentUserEmail
            userEmail?.let {
                val user = userRepository.getUserByEmail(it)
                user?.let {
                    FavoriteCitiesScreen(
                        favoriteCitiesViewModel = remember { FavoriteCitiesViewModel() },
                        navController = navController,
                        user = it,
                        userRepository = userRepository
                    )
                }
            }
        }
    }
}
