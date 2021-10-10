package com.example.mycinemaapp.model.repository

import com.example.mycinemaapp.model.CinemaDetailsDTO
import retrofit2.Callback


interface DetailsRepository {
    fun getCinemaDetailsFromServer(id: Int, callback: Callback<CinemaDetailsDTO>)
}