package com.example.mycinemaapp.viewmodel

import com.example.mycinemaapp.model.Cinema
import com.example.mycinemaapp.model.CinemaDTO
import com.example.mycinemaapp.model.CinemaDetailsDTO
import com.example.mycinemaapp.model.CinemaType

sealed class AppState {
    class Success(val cinemaDTO: CinemaDTO, val cinemaType: CinemaType) : AppState()
    class SuccessDetails(val cinemaDetailsDTO: CinemaDetailsDTO) : AppState()
    class SuccessDetailsHistory(val cinema: List<Cinema>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}