package com.example.mycinemaapp.viewmodel

import com.example.mycinemaapp.model.Cinema

sealed class AppState {
    data class SuccessBest(val cinemaData: List<Cinema>) : AppState()
    data class SuccessUpcoming(val cinemaData: List<Cinema>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}