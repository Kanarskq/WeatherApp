package com.example.weatherapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.dtos.User
import com.example.weatherapp.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val userRepository: UserRepository, userEmail: String) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    init {
        viewModelScope.launch {
            _user.value = userRepository.getUserByEmail(userEmail)
        }
    }

    fun updateUserTemperatureUnit(isCelsius: Boolean) {
        val currentUser = _user.value ?: return
        val updatedUser = currentUser.copy(tempUnit = if (isCelsius) "celsius" else "fahrenheit")
        viewModelScope.launch {
            userRepository.updateUser(updatedUser)
            _user.value = updatedUser
        }
    }
}