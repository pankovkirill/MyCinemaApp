package com.example.mycinemaapp.model.repository

import com.example.mycinemaapp.model.Cinema
import com.example.mycinemaapp.model.CinemaDetailsDTO

interface LocalRepository {
    fun getAllHistory(): List<Cinema>
    fun getDataByFilm(film: String): Boolean
    fun saveEntity(cinema: Cinema)
    fun delete(film: String)
    fun deleteAll()
}