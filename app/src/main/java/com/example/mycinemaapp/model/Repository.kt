package com.example.mycinemaapp.model

interface Repository {
    fun getTopCinema(listener: CinemaListLoader.CinemaListLoaderListener)
    fun getNewCinema(listener: CinemaListLoader.CinemaListLoaderListener)
}