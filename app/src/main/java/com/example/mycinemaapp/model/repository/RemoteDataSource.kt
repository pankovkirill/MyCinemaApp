package com.example.mycinemaapp.model.repository

import android.graphics.Region
import com.example.mycinemaapp.BuildConfig
import com.example.mycinemaapp.model.CinemaDTO
import com.example.mycinemaapp.model.CinemaDetailsDTO
import com.google.gson.GsonBuilder
import org.intellij.lang.annotations.Language
import retrofit2.Callback


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {
    private val cinemaApi = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(
            GsonConverterFactory.create(GsonBuilder().setLenient().create())
        )
        .build()
        .create(CinemaAPI::class.java)

    fun getCinemaDetails(id: Int, callback: Callback<CinemaDetailsDTO>) {
        cinemaApi.getCinema(id, BuildConfig.API_KEY, "ru-RU", "ru").enqueue(callback)
    }

    fun getTopCinema(page: Int, callback: Callback<CinemaDTO>) {
        cinemaApi.getTopCinema(BuildConfig.API_KEY, "ru-RU", "ru", page).enqueue(callback)
    }

    fun getNewCinema(page: Int, callback: Callback<CinemaDTO>) {
        cinemaApi.getNewCinema(BuildConfig.API_KEY, "ru-RU", "ru", page).enqueue(callback)
    }
}