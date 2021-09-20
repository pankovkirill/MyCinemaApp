package com.example.mycinemaapp.view

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.mycinemaapp.BuildConfig
import com.example.mycinemaapp.model.CinemaDetailsDTO
import com.example.mycinemaapp.model.Genre
import com.example.mycinemaapp.model.ProductionCompany
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val ID_EXTRA = "Id"
private const val REQUEST_GET = "GET"
private const val REQUEST_TIMEOUT = 10000

class DetailsService(name: String = "DetailsService") : IntentService(name) {

    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            onEmptyIntent()
        } else {
            val id = intent.getIntExtra(ID_EXTRA, 0)
            if (id == 0) {
                onEmptyData()
            } else {
                loadCinema(id)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadCinema(id: Int) {
        try {
            val uri =
                URL("https://api.themoviedb.org/3/movie/$id?api_key=${BuildConfig.API_KEY}&language=ru-RU")
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection?.apply {
                    requestMethod = REQUEST_GET
                    readTimeout = REQUEST_TIMEOUT
                }
                val cinemaDetailsDTO: CinemaDetailsDTO = Gson().fromJson(
                    getLines(BufferedReader(InputStreamReader(urlConnection.inputStream))),
                    CinemaDetailsDTO::class.java
                )
                onResponse(cinemaDetailsDTO)
            } catch (e: Exception) {
                onErrorRequest(e.message ?: "Empty error")
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            onMalformedURL()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader) = reader.lines().collect(Collectors.joining("\n"))

    private fun onResponse(cinemaDetailsDTO: CinemaDetailsDTO) {
        onSuccessResponse(
            cinemaDetailsDTO.budget,
            cinemaDetailsDTO.genres,
            cinemaDetailsDTO.overview,
            cinemaDetailsDTO.production_companies,
            cinemaDetailsDTO.release_date,
            cinemaDetailsDTO.runtime,
            cinemaDetailsDTO.revenue,
            cinemaDetailsDTO.title,
            cinemaDetailsDTO.vote_average
        )
    }

    private fun onSuccessResponse(
        budget: Int?,
        genres: ArrayList<Genre>?,
        overview: String?,
        production_companies: ArrayList<ProductionCompany>?,
        release_date: String?,
        runtime: Int?,
        revenue: Int?,
        title: String?,
        vote_Average: Double?
    ) {
        putLoadResult(DETAILS_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(DETAILS_BUDGET_EXTRA, budget)
        broadcastIntent.putParcelableArrayListExtra(DETAILS_GENRES_EXTRA, genres)
        broadcastIntent.putExtra(DETAILS_OVERVIEW_EXTRA, overview)
        broadcastIntent.putParcelableArrayListExtra(DETAILS_PRODUCTION_COMPANIES_EXTRA, production_companies)
        broadcastIntent.putExtra(DETAILS_RELEASE_DATE_EXTRA, release_date)
        broadcastIntent.putExtra(DETAILS_RUNTIME_EXTRA, runtime)
        broadcastIntent.putExtra(DETAILS_REVENUE_EXTRA, revenue)
        broadcastIntent.putExtra(DETAILS_TITLE_EXTRA, title)
        broadcastIntent.putExtra(DETAILS_VOTE_AVERAGE_EXTRA, vote_Average)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onMalformedURL() {
        putLoadResult(DETAILS_URL_MALFORMED_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, result)
    }

    private fun onErrorRequest(error: String) {
        putLoadResult(DETAILS_REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(DETAILS_REQUEST_ERROR_MESSAGE_EXTRA, error)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)

    }

    private fun onEmptyData() {
        putLoadResult(DETAILS_DATA_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)

    }

    private fun onEmptyIntent() {
        putLoadResult(DETAILS_INTENT_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)

    }
}