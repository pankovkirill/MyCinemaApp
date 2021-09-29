package com.example.mycinemaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycinemaapp.model.*
import java.lang.Exception
import java.lang.Thread.sleep
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    private val repository: Repository = RepositoryImpl()
    private val cinemaListener: CinemaListLoader.CinemaListLoaderListener =
        object : CinemaListLoader.CinemaListLoaderListener {
            override fun onLoaded(cinemaDTO: CinemaDTO, cinemaType: CinemaType) {
                liveDataToObserve.postValue(AppState.Success(cinemaDTO, cinemaType))
            }

            override fun onFailed(throwable: Throwable) {
                liveDataToObserve.postValue(AppState.Error(throwable))
            }

        }

    fun getCinemaLiveData() = liveDataToObserve

    fun getData() {
        getTopCinemaList()
        getNewCinemaList()
    }

    private fun getNewCinemaList() {
        repository.getNewCinema(cinemaListener)
    }

    private fun getTopCinemaList() {
        repository.getTopCinema(cinemaListener)
    }

}