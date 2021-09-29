package com.example.mycinemaapp.model.repository

import com.example.mycinemaapp.model.Cinema
import com.example.mycinemaapp.model.dataBase.HistoryDao
import com.example.mycinemaapp.model.dataBase.HistoryEntity
import com.example.mycinemaapp.model.utils.convertCinemaToEntity
import com.example.mycinemaapp.model.utils.convertHistoryEntityToCinema

class LocalRepositoryImpl(private val localDataSource: HistoryDao) : LocalRepository {
    override fun getAllHistory(): List<Cinema> {
        return convertHistoryEntityToCinema(localDataSource.all())
    }

    override fun getDataByFilm(film: String): Boolean {
        val entity: List<HistoryEntity> = localDataSource.getDataByFilm(film)
        return entity.isEmpty()
    }

    override fun saveEntity(cinema: Cinema) {
        localDataSource.insert(convertCinemaToEntity(cinema))
    }

    override fun delete(film: String) {
        localDataSource.deleteById(film)
    }

    override fun deleteAll() {
        localDataSource.deleteAll()
    }
}