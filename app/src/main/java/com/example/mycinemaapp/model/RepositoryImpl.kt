package com.example.mycinemaapp.model

class RepositoryImpl : Repository {

    override fun getCinemaFromLocalStorage(): Cinema = Cinema()

    override fun getCinemaFromServer(): Cinema = Cinema()
}