package com.example.mycinemaapp.model.utils

import com.example.mycinemaapp.model.*
import com.example.mycinemaapp.model.dataBase.HistoryEntity

fun convertHistoryEntityToCinema(entityList: List<HistoryEntity>): List<Cinema> {
    return entityList.map {
        Cinema(it.id, it.film, it.release, it.average, it.note)
    }
}

fun convertCinemaToEntity(cinema: Cinema): HistoryEntity {
    with(cinema) {
        return HistoryEntity(
            0,
            film,
            release,
            average,
            note
        )
    }
}