package com.example.mycinemaapp.model

import android.os.Build
import androidx.annotation.RequiresApi

class RepositoryImpl : Repository {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun getTopCinema(listener: CinemaListLoader.CinemaListLoaderListener) {
        val loader = CinemaListLoader(listener, CinemaType.TOP)
        loader.loadCinema()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getNewCinema(listener: CinemaListLoader.CinemaListLoaderListener) {
        val loader = CinemaListLoader(listener, CinemaType.NEW)
        loader.loadCinema()
    }
}