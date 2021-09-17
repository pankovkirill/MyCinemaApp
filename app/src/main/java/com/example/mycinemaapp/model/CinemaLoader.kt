package com.example.mycinemaapp.model

import android.os.Build
import android.os.Handler
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

private const val API_KEY = BuildConfig.API_KEY

class CinemaLoader(private val listener: CinemaLoaderListener, private val id: Int) {


    @RequiresApi(Build.VERSION_CODES.N)
    fun loadCinema() {
        try {
            val uri =
                URL("https://api.themoviedb.org/3/movie/${id}?api_key=${API_KEY}")
            val handler = Handler()
            Thread(Runnable {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection?.apply {
                        requestMethod = "GET"
                        readTimeout = 10000
                        addRequestProperty(
                            "api_key",
                            API_KEY
                        )
                    }
                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val cinemaDTO: CinemaDTO =
                        Gson().fromJson(getLines(bufferedReader), CinemaDTO::class.java)
                    handler.post { listener.onLoaded(cinemaDTO) }
                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace()
                    listener.onFailed(Throwable())
                } finally {
                    urlConnection.disconnect()
                }
            }).start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URL", e)
            e.printStackTrace()
            listener.onFailed(Throwable())
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(bufferedReader: BufferedReader): String {
        return bufferedReader.lines().collect(Collectors.joining("\n"))

    }

    interface CinemaLoaderListener {
        fun onLoaded(cinemaDTO: CinemaDTO)
        fun onFailed(throwable: Throwable)
    }
}