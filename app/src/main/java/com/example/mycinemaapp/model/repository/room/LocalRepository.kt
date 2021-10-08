package com.example.mycinemaapp.model.repository.room

import com.example.mycinemaapp.model.Cinema
import com.example.mycinemaapp.model.CinemaDetailsDTO
import com.example.mycinemaapp.model.dataBase.HistoryEntity

interface LocalRepository {
    fun getAllHistory(): List<Cinema>
    fun getDataByFilm(film: String): Boolean
    fun getNoteByFilm(film: String): String
    fun saveEntity(cinema: Cinema)
    fun delete(film: String)
    fun deleteAll()
}