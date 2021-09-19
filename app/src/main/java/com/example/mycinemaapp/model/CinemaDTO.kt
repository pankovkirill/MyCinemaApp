package com.example.mycinemaapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CinemaDTO(
    val page: Int,
    val results: List<CinemaPreview>,
    val total_pages: Int,
    val total_results: Int
) : Parcelable {
    @Parcelize
    data class CinemaPreview(
        val id: Int =1,
        val release_date: String = "",
        val title: String = "",
        val vote_average: Float = 0.0F,
    ) : Parcelable
}