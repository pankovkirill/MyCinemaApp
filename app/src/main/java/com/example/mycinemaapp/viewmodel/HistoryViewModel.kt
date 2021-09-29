package com.example.mycinemaapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycinemaapp.app.App.Companion.getHistoryDao
import com.example.mycinemaapp.model.repository.LocalRepositoryImpl
import com.example.mycinemaapp.model.repository.LocalRepository

class HistoryViewModel(
    val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepository: LocalRepository = LocalRepositoryImpl(getHistoryDao())
) : ViewModel() {
    fun getAllHistory() {
        historyLiveData.value = AppState.Loading
        historyLiveData.value = AppState.SuccessDetailsHistory(historyRepository.getAllHistory())
    }
}