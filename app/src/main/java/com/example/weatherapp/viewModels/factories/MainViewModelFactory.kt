package com.example.weatherapp.viewModels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.dtos.User
import com.example.weatherapp.viewModels.MainViewModel

class MainViewModelFactory(private val city: String, private val user: User) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(city, user) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
