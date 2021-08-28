package com.example.mycinemaapp.viewmodel

import com.example.mycinemaapp.model.Cinema

sealed class AppState {
    data class Success(val cinema: Cinema) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}