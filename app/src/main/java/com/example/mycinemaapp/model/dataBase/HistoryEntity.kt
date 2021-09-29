package com.example.mycinemaapp.model.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val film: String,
    val release: String,
    val average: Double,
    val note: String
)