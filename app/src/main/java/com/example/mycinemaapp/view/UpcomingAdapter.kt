package com.example.mycinemaapp.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycinemaapp.R
import com.example.mycinemaapp.model.Cinema

class UpcomingAdapter(private var onItemViewClickListener: MainFragment.OnItemViewClickListener?) :
    RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder>() {

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
    ): UpcomingViewHolder =
        UpcomingViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_upcoming_recycler_item, parent, false)
        )

    override fun onBindViewHolder(holder: UpcomingAdapter.UpcomingViewHolder, position: Int) {
        holder.bind(cinemaData[position])
    }

    override fun getItemCount(): Int = cinemaData.size

    inner class UpcomingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(cinema: Cinema) {
            itemView.apply {
                findViewById<TextView>(R.id.recyclerItemFilm).text = cinema.film.film
                findViewById<TextView>(R.id.recyclerItemDate).text = cinema.film.year.toString()
                findViewById<ImageView>(R.id.imageView).setImageResource(cinema.film.filmImage)
                setOnClickListener {
                    onItemViewClickListener?.onItemViewClick(cinema)
                }
            }
        }
    }
}