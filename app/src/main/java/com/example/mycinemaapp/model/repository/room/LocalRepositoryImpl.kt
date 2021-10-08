package com.example.mycinemaapp.model.repository.room

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

    override fun getNoteByFilm(film: String): String {
        val entity: HistoryEntity = localDataSource.getNoteByFilm(film)
        return entity.note
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