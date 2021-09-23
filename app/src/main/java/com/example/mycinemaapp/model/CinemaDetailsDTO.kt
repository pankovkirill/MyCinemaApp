package com.example.mycinemaapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class CinemaDetailsDTO(
    val budget: Int,
    val genres: @RawValue ArrayList<Genre>,
    val id: Int,
    val overview: String,
    val production_companies: @RawValue ArrayList<ProductionCompany>,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val title: String,
    val vote_average: Double,
    val poster_path: String
) : Parcelable

@Parcelize
data class Genre(
    val id: Int = 0,
    val name: String = ""
) : Parcelable

@Parcelize
data class ProductionCompany(
    val name: String,
    val origin_country: String
) : Parcelable