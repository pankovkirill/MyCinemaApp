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

class SearchViewModel(
    val searchLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: RepositoryImpl = RepositoryImpl(RemoteDataSource()),
) : ViewModel() {

    fun getCinema() {
        searchLiveData.value = AppState.Loading
        repository.getTopCinema(1, callback)
    }


    private val callback = object : Callback<CinemaDTO> {
        override fun onResponse(
            call: Call<CinemaDTO>,
            response: Response<CinemaDTO>
        ) {
            val serverResponse: CinemaDTO? = response.body()
            searchLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<CinemaDTO>, e: Throwable) {
            AppState.Error(e)
        }
    }

    private fun checkResponse(serverResponse: CinemaDTO): AppState {
        return AppState.SuccessSearch(serverResponse)
    }
}