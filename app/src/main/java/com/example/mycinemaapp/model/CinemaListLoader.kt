package com.example.mycinemaapp.model

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.mycinemaapp.BuildConfig
import com.example.mycinemaapp.view.showSnackBar
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class CinemaListLoader(
    private val listener: CinemaListLoaderListener,
    private val cinemaType: CinemaType
) {


    @RequiresApi(Build.VERSION_CODES.N)
    fun loadCinema() {
        val uri = cinemaType.uri
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
            val cinemaDTO: CinemaDTO =
                Gson().fromJson(
                    reader.lines().collect(Collectors.joining("\n")),
                    CinemaDTO::class.java
                )
            handler.post { listener.onLoaded(cinemaDTO, cinemaType) }
        } catch (e: Exception) {
            Log.e("", "Fail connection", e)
            e.printStackTrace()
            listener.onFailed(e)
        } finally {
            urlConnection.disconnect()
        }
    }


    interface CinemaListLoaderListener {
        fun onLoaded(cinemaDTO: CinemaDTO, cinemaType: CinemaType)
        fun onFailed(throwable: Throwable)
    }
}