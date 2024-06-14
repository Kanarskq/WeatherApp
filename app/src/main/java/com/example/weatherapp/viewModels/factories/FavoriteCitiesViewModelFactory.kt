package com.example.weatherapp.viewModels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.dtos.User
import com.example.weatherapp.repositories.UserRepository
import com.example.weatherapp.viewModels.FavoriteCitiesViewModel

class FavoriteCitiesViewModelFactory(
    private val userRepository: UserRepository,
    private val user: User
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteCitiesViewModel::class.java)) {
            return FavoriteCitiesViewModel(userRepository, user) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
