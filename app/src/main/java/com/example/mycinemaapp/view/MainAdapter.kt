package com.example.mycinemaapp.view

import android.annotation.SuppressLint
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycinemaapp.R
import com.example.mycinemaapp.model.Cinema
import kotlin.reflect.typeOf

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

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(cinemaData[position])
    }

    override fun getItemCount(): Int = cinemaData.size

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(cinema: Cinema) {
            itemView.apply {
                findViewById<TextView>(R.id.recyclerItemFilm).text = cinema.film.film
                findViewById<TextView>(R.id.recyclerItemRating).text = cinema.rating.toString()
                findViewById<ImageView>(R.id.imageView).setImageResource(cinema.film.filmImage)
                setOnClickListener {
                    onItemViewClickListener?.onItemViewClick(cinema)
                }
            }
        }
    }
}