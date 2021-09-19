package com.example.mycinemaapp.viewmodel

import com.example.mycinemaapp.model.CinemaDTO
import com.example.mycinemaapp.model.CinemaType

sealed class AppState {
    data class Success(val cinemaDTO: CinemaDTO, val cinemaType: CinemaType) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}