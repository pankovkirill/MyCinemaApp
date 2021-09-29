package com.example.mycinemaapp.model

import android.text.Editable

data class Cinema(
    val id: Int,
    val film: String,
    val release: String,
    val average: Double,
    val note: String
)