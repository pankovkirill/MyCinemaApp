package com.example.mycinemaapp.view.main

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.mycinemaapp.R
import com.example.mycinemaapp.model.CinemaDTO


class NewCinemaAdapter(private var onItemViewClickListener: MainFragment.OnItemViewClickListener?) :
    RecyclerView.Adapter<NewCinemaAdapter.NewViewHolder>() {

    private var cinemaData: List<CinemaDTO.CinemaPreview> = listOf()
    fun setCinema(data: List<CinemaDTO.CinemaPreview>) {
        cinemaData = data
        notifyDataSetChanged()
    }

    fun removeListener() {
        onItemViewClickListener = null
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewViewHolder =
        NewViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_item, parent, false)
        )

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        holder.bind(cinemaData[position])
    }

    override fun getItemCount(): Int = cinemaData.size

    inner class NewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(cinemaDTO: CinemaDTO.CinemaPreview) {
            if (!cinemaDTO.adult) {
                itemView.apply {
                    findViewById<TextView>(R.id.recyclerItemFilm).text = cinemaDTO.title
                    findViewById<TextView>(R.id.recyclerItemDate).text =
                        cinemaDTO.release_date.substringBefore("-")
                    findViewById<TextView>(R.id.recyclerItemAverage).text =
                        cinemaDTO.vote_average.toString()
                    findViewById<ImageView>(R.id.imageView).load("https://image.tmdb.org/t/p/w500/${cinemaDTO.poster_path}")
                    setOnClickListener {
                        onItemViewClickListener?.onItemViewClick(cinemaDTO)
                    }
                }
            } else itemView.visibility = View.GONE
        }
    }
}