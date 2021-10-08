package com.example.mycinemaapp.model.repository.main

import com.example.mycinemaapp.model.CinemaDTO
import retrofit2.Callback

interface Repository {
    fun getTopCinema(page: Int, callback: Callback<CinemaDTO>)
    fun getNewCinema(page: Int, callback: Callback<CinemaDTO>)
}