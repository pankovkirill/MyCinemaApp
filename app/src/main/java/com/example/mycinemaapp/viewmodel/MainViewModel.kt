package com.example.mycinemaapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycinemaapp.model.*
import com.example.mycinemaapp.model.repository.RemoteDataSource
import com.example.mycinemaapp.model.repository.Repository
import com.example.mycinemaapp.model.repository.RepositoryImpl
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

private const val SERVER_ERROR = "Ошибка сервера"

class MainViewModel(
    val topCinemaToObserve: MutableLiveData<AppState> = MutableLiveData(),
    val newCinemaToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: RepositoryImpl = RepositoryImpl(RemoteDataSource()),
) : ViewModel() {

    fun getCinema() {
        getCinemaByCategories(topCinemaToObserve, CinemaType.TOP)
        getCinemaByCategories(newCinemaToObserve, CinemaType.NEW)
    }

    private fun getCinemaByCategories(
        observe: MutableLiveData<AppState>,
        cinemaType: CinemaType,
    ) {
        val callback = object : Callback<CinemaDTO> {
            override fun onResponse(call: Call<CinemaDTO>, response: Response<CinemaDTO>) {
                val serverResponse: CinemaDTO? = response.body()
                observe.postValue(
                    if (response.isSuccessful && serverResponse != null) {
                        AppState.Success(
                            serverResponse,
                            cinemaType
                        )
                    } else {
                        AppState.Error(Throwable(SERVER_ERROR))
                    }
                )
            }

            override fun onFailure(call: Call<CinemaDTO>, e: Throwable) {
                AppState.Error(e)
            }
        }
        when (cinemaType) {
            CinemaType.TOP -> {
                repository.getTopCinema(1, callback)
            }
            CinemaType.NEW -> {
                repository.getNewCinema(1, callback)
            }
        }
    }
}