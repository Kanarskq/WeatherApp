package com.example.weatherapp.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.dtos.User
import com.example.weatherapp.repositories.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var errorMessage = mutableStateOf("")

    fun registerUser(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val newUser = User(id = 0, email = email.value, password = password.value)
            if (userRepository.addUser(newUser)) {
                onSuccess()
            } else {
                errorMessage.value = "Email already exists"
            }
        }
    }
}