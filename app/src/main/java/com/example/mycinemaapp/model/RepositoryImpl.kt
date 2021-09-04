package com.example.mycinemaapp.model

class RepositoryImpl : Repository {

    override fun getCinemaFromLocalStorageBest(): List<Cinema> {
        return getBestCinema()
    }

    override fun getCinemaFromLocalStorageUpcoming(): List<Cinema> {
        return getUpcomingCinema()
    }

    override fun getCinemaFromServer(): Cinema = Cinema()
}