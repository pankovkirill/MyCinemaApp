package com.example.mycinemaapp.model

import com.example.mycinemaapp.BuildConfig
import java.net.URL

enum class CinemaType(val uri: URL) {
    TOP(URL("https://api.themoviedb.org/3/movie/top_rated?api_key=${BuildConfig.API_KEY}&language=ru-RU&page=1")),
    NEW(URL("https://api.themoviedb.org/3/movie/now_playing?api_key=${BuildConfig.API_KEY}&language=ru-RU&page=1"))
}