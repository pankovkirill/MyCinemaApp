package com.example.mycinemaapp.model.repository

import com.example.mycinemaapp.model.CinemaDetailsDTO
import retrofit2.Callback


class DetailsRepositoryImp(private val remoteDataSource: RemoteDataSource) : DetailsRepository {
    override fun getCinemaDetailsFromServer(id: Int, callback: Callback<CinemaDetailsDTO>) {
        remoteDataSource.getCinemaDetails(id, callback)
    }
}