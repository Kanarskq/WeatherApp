package com.example.weatherapp.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.UserSession
import com.example.weatherapp.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var errorMessage = mutableStateOf("")

    fun login(onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                userRepository.getUserByEmail(email.value)
            }
            if (user != null && user.password == password.value) {
                UserSession.currentUserEmail = email.value
                if (user.favoriteCities.isNotEmpty()) {
                    onSuccess("main/${user.favoriteCities.first()}")
                } else {
                    onSuccess("welcome")
                }
            } else {
                onFailure("Invalid email or password")
            }
        }
    }
}