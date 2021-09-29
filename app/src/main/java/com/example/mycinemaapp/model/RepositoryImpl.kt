package com.example.mycinemaapp.model

class RepositoryImpl : Repository {

    override fun getCinemaFromLocalStorageBest(): List<Cinema> = getBestCinema()

    override fun getCinemaFromLocalStorageUpcoming(): List<Cinema> = getUpcomingCinema()

    override fun getCinemaFromServer(): Cinema = Cinema()
}