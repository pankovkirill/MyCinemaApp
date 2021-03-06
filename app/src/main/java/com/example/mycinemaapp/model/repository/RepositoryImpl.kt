package com.example.mycinemaapp.model.repository

import com.example.mycinemaapp.model.CinemaDTO
import retrofit2.Callback

class RepositoryImpl(
        private val remoteDataSource: RemoteDataSource
) : Repository {
    override fun getTopCinema(page: Int, callback: Callback<CinemaDTO>) {
        remoteDataSource.getTopCinema(page, callback)
    }

    override fun getNewCinema(page: Int, callback: Callback<CinemaDTO>) {
        remoteDataSource.getNewCinema(page, callback)
    }
}