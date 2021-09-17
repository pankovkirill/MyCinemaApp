package com.example.mycinemaapp.model

import android.os.Parcelable
import com.example.mycinemaapp.view.showSnackBar
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

@Parcelize
data class CinemaDTO(
    val original_title: String?,
    val overview: String?,
    val genres: Array<GenresDTO?>,
    val release_date: String?
):Parcelable
@Parcelize
data class GenresDTO(
    val name: String?
):Parcelable