package com.example.mycinemaapp.view

import android.annotation.SuppressLint
import android.content.res.TypedArray
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.mycinemaapp.BuildConfig
import com.example.mycinemaapp.R
import com.example.mycinemaapp.model.Cinema
import com.example.mycinemaapp.model.CinemaDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection
import kotlin.reflect.typeOf

private const val API_KEY = BuildConfig.API_KEY

class MainAdapter(private var onItemViewClickListener: MainFragment.OnItemViewClickListener?) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    var cinemaData: List<Cinema> = listOf()
    fun removeListener() {
        onItemViewClickListener = null
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCinema(data: List<Cinema>) {
        cinemaData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainViewHolder =
        MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_main_recycler_item, parent, false)
        )

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(cinemaData[position])
    }

    override fun getItemCount(): Int = cinemaData.size

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(cinema: Cinema) {
            try {
                val uri =
                    URL("https://api.themoviedb.org/3/movie/${cinema.id}?api_key=$API_KEY")
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
                        handler.post { onLoaded(cinemaDTO, itemView) }
                    } catch (e: Exception) {
                        Log.e("", "Fail connection", e)
                        e.printStackTrace()
                    } finally {
                        urlConnection.disconnect()
                    }
                }).start()
            } catch (e: MalformedURLException) {
                Log.e("", "Fail URL", e)
                e.printStackTrace()
                //listener.onFailed(Throwable())
            }
            itemView.setOnClickListener {
                onItemViewClickListener?.onItemViewClick(cinema)
            }
        }
    }

    private fun onLoaded(cinemaDTO: CinemaDTO, itemView: View) {
        itemView.apply {
            findViewById<TextView>(R.id.recyclerItemFilm).text = cinemaDTO.original_title
            findViewById<TextView>(R.id.recyclerItemDate).text =
                cinemaDTO.release_date?.substringBefore("-")
            findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.empty)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(bufferedReader: BufferedReader): String {
        return bufferedReader.lines().collect(Collectors.joining("\n"))
    }


}