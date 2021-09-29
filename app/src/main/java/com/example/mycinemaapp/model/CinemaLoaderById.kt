package com.example.mycinemaapp.model

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.mycinemaapp.BuildConfig
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class CinemaLoaderById(
    private val listener: CinemaLoaderById.CinemaLoaderListener,
    private val id: Int
) {


    @RequiresApi(Build.VERSION_CODES.N)
    fun loadCinema() {
        val uri =
            URL("https://api.themoviedb.org/3/movie/$id?api_key=${BuildConfig.API_KEY}&language=ru-RU")
        Thread {
            goToInternet(uri)
        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun goToInternet(uri: URL) {
        val handler = Handler(Looper.getMainLooper())
        lateinit var urlConnection: HttpsURLConnection
        try {
            urlConnection = uri.openConnection() as HttpsURLConnection
            urlConnection?.apply {
                requestMethod = "GET"
                readTimeout = 10000
            }
            val reader =
                BufferedReader(InputStreamReader(urlConnection.inputStream))
            val cinemaDetailsDTO: CinemaDetailsDTO =
                Gson().fromJson(
                    reader.lines().collect(Collectors.joining("\n")),
                    CinemaDetailsDTO::class.java
                )
            handler.post { listener.onLoaded(cinemaDetailsDTO) }
        } catch (e: Exception) {
            Log.e("", "Fail connection", e)
            e.printStackTrace()
            listener.onFailed(e)
        } finally {
            urlConnection.disconnect()
        }
    }


    interface CinemaLoaderListener {
        fun onLoaded(cinemaDetailsDTO: CinemaDetailsDTO)
        fun onFailed(throwable: Throwable)
    }
}