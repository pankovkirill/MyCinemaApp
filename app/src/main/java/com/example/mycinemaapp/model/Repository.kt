package com.example.mycinemaapp.model

interface Repository {
    fun getCinemaFromServer(): Cinema
    fun getCinemaFromLocalStorageBest(): List<Cinema>
    fun getCinemaFromLocalStorageUpcoming(): List<Cinema>
}