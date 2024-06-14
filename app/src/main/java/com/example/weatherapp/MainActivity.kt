package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.weatherapp.viewModels.LoginViewModel
import com.example.weatherapp.viewModels.RegisterViewModel
import com.example.weatherapp.viewModels.SettingsViewModel
import com.example.weatherapp.viewModels.WelcomeViewModel
import com.example.weatherapp.viewModels.factories.FavoriteCitiesViewModelFactory
import com.example.weatherapp.viewModels.factories.LoginViewModelFactory
import com.example.weatherapp.viewModels.factories.RegisterViewModelFactory
import com.example.weatherapp.viewModels.factories.SettingsViewModelFactory
import com.example.weatherapp.viewModels.factories.WelcomeViewModelFactory

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
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(userRepository)
            )
            LoginScreen(navController = navController, loginViewModel = loginViewModel)
        }
        composable("register") {
            val registerViewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModelFactory(userRepository)
            )
            RegisterScreen(navController = navController, registerViewModel = registerViewModel)
        }
        composable("welcome") {
            val welcomeViewModel: WelcomeViewModel = viewModel(
                factory = WelcomeViewModelFactory(userRepository)
            )
            WelcomeScreen(navController = navController, welcomeViewModel = welcomeViewModel)
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
                val settingsViewModel: SettingsViewModel = viewModel (
                    factory = SettingsViewModelFactory(userRepository, userEmail)
                )
                SettingsScreen(navController = navController, settingsViewModel = settingsViewModel)
            }
        }
        composable("favorite_cities") {
            val userEmail = UserSession.currentUserEmail
            userEmail?.let {
                val user = userRepository.getUserByEmail(it)
                user?.let {
                    val favoriteCitiesViewModel: FavoriteCitiesViewModel = viewModel (
                        factory = FavoriteCitiesViewModelFactory(userRepository, user)
                    )
                    FavoriteCitiesScreen(
                        favoriteCitiesViewModel = favoriteCitiesViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}
