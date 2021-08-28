package com.example.mycinemaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycinemaapp.model.Repository
import com.example.mycinemaapp.model.RepositoryImpl
import java.lang.Exception
import java.lang.Thread.sleep
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    private val repository: Repository = RepositoryImpl()

    val liveData: LiveData<AppState> = liveDataToObserve

    fun getCinemaFromLocalSource() = getDataFromLocalSource()
    fun getCinemaFromRemoteSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading

        Thread {
            sleep(5000)
            if (Random.nextBoolean()) {
                liveDataToObserve.postValue(AppState.Success(repository.getCinemaFromLocalStorage()))
            } else {
                liveDataToObserve.postValue(AppState.Error(Exception("Нет доступа к интернету")))
            }
        }.start()
    }
}