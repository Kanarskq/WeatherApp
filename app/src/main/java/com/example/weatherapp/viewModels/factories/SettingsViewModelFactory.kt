package com.example.weatherapp.viewModels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.repositories.UserRepository
import com.example.weatherapp.viewModels.SettingsViewModel

class SettingsViewModelFactory(
    private val userRepository: UserRepository,
    private val userEmail: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(userRepository, userEmail) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}