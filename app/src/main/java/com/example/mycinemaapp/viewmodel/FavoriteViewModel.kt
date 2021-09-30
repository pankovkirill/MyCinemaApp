package com.example.mycinemaapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycinemaapp.app.App.Companion.getHistoryDao
import com.example.mycinemaapp.model.repository.LocalRepositoryImpl
import com.example.mycinemaapp.model.repository.LocalRepository

class FavoriteViewModel(
    val favoriteLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val favoriteRepository: LocalRepository = LocalRepositoryImpl(getHistoryDao())
) : ViewModel() {
    fun getAllHistory() {
        favoriteLiveData.value = AppState.Loading
        favoriteLiveData.value = AppState.SuccessFavorite(favoriteRepository.getAllHistory())
    }
}