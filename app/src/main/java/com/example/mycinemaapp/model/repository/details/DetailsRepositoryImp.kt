package com.example.mycinemaapp.model.repository.details

import com.example.mycinemaapp.model.CinemaDetailsDTO
import com.example.mycinemaapp.model.repository.retrofit.RemoteDataSource
import retrofit2.Callback


class DetailsRepositoryImp(private val remoteDataSource: RemoteDataSource) : DetailsRepository {
    override fun getCinemaDetailsFromServer(id: Int, callback: Callback<CinemaDetailsDTO>) {
        remoteDataSource.getCinemaDetails(id, callback)
    }
}