package com.example.mycinemaapp.model

interface Repository {
    fun getCinemaFromServer(): Cinema
    fun getCinemaFromLocalStorage(): Cinema
}