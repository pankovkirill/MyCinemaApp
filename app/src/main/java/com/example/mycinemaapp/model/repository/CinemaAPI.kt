package com.example.mycinemaapp.model.repository

import com.example.mycinemaapp.BuildConfig
import com.example.mycinemaapp.model.CinemaDTO
import com.example.mycinemaapp.model.CinemaDetailsDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CinemaAPI {
    @GET("3/movie/{id}?")
    fun getCinema(
            @Path("id") id: Int,
            @Query("api_key") api_key: String,
            @Query("language") language: String,
            @Query("region") region: String,
            ): Call<CinemaDetailsDTO>

    @GET("3/movie/top_rated?")
    fun getTopCinema(
            @Query("api_key") api_key: String,
            @Query("language") language: String,
            @Query("region") region: String,
            @Query("page") page: Int
            ): Call<CinemaDTO>

    @GET("3/movie/now_playing?")
    fun getNewCinema(
            @Query("api_key") api_key: String,
            @Query("language") language: String,
            @Query("region") region: String,
            @Query("page") page: Int
            ): Call<CinemaDTO>
}
