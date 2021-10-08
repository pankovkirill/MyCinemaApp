package com.example.mycinemaapp.model.dataBase

import androidx.room.*

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HistoryEntity")
    fun all(): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity WHERE film LIKE :film")
    fun getDataByFilm(film: String): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity WHERE film LIKE :film")
    fun getNoteByFilm(film: String): HistoryEntity

    @Query("DELETE FROM HistoryEntity WHERE film =:film")
    fun deleteById(film: String)

    @Query("DELETE FROM HistoryEntity")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryEntity)

    @Update
    fun update(entity: HistoryEntity)

    @Delete
    fun delete(entity: HistoryEntity)
}