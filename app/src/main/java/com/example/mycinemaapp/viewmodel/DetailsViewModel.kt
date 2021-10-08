package com.example.mycinemaapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycinemaapp.model.CinemaDetailsDTO
import com.example.mycinemaapp.model.repository.room.LocalRepositoryImpl
import com.example.mycinemaapp.model.repository.details.DetailsRepositoryImp
import com.example.mycinemaapp.model.repository.room.LocalRepository
import com.example.mycinemaapp.model.repository.retrofit.RemoteDataSource
import com.example.mycinemaapp.app.App.Companion.getHistoryDao
import com.example.mycinemaapp.model.Cinema
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


import kotlin.jvm.Throws

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"

class DetailsViewModel(
    val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImp: DetailsRepositoryImp = DetailsRepositoryImp(RemoteDataSource()),
    private val favoriteRepository: LocalRepository = LocalRepositoryImpl(getHistoryDao())
) : ViewModel() {

    fun getCinema(id: Int) {
        detailsLiveData.value = AppState.Loading
        detailsRepositoryImp.getCinemaDetailsFromServer(id, callback)
    }

    fun saveFilmToDB(cinema: Cinema) {
        favoriteRepository.saveEntity(cinema)
    }

    private val callback = object : Callback<CinemaDetailsDTO> {
        @Throws(IOException::class)
        override fun onResponse(
            call: Call<CinemaDetailsDTO>,
            response: Response<CinemaDetailsDTO>
        ) {
            val serverResponse: CinemaDetailsDTO? = response.body()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<CinemaDetailsDTO>, e: Throwable?) {
            detailsLiveData.postValue(AppState.Error(Throwable(e?.message ?: REQUEST_ERROR)))
        }

        private fun checkResponse(serverResponse: CinemaDetailsDTO): AppState {
            return AppState.SuccessDetails(serverResponse)
        }

    }

}