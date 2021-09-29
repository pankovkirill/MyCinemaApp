package com.example.mycinemaapp.model

import android.accounts.AuthenticatorDescription
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film(
    val film: String,
    val year: Int,
    val genre: String,
    val description: String
) : Parcelable