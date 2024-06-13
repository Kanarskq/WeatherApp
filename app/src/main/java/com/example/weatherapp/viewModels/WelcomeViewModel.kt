package com.example.weatherapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.repositories.UserRepository
import kotlinx.coroutines.launch

class WelcomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun addCityToUserFavorites(email: String, city: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email)
            user?.let {
                val favoriteCities = it.favoriteCities.toMutableList()
                favoriteCities.add(0, city)
                it.favoriteCities = favoriteCities
                userRepository.updateUser(it)
                onSuccess()
            }
        }
    }
}