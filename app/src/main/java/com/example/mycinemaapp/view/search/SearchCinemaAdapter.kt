package com.example.mycinemaapp.view.search

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.mycinemaapp.R
import com.example.mycinemaapp.model.CinemaDTO

class SearchCinemaAdapter(private var onItemViewClickListener: SearchFragment.OnItemViewClickListener?) :
    RecyclerView.Adapter<SearchCinemaAdapter.TopViewHolder>() {

    private var cinemaData: List<CinemaDTO.CinemaPreview> = listOf()
    private var adultContent = false
    fun setCinema(data: List<CinemaDTO.CinemaPreview>, adult: Boolean) {
        adultContent = adult
        cinemaData = data
        notifyDataSetChanged()
    }

    fun removeListener() {
        onItemViewClickListener = null
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopViewHolder =
        TopViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_search_item, parent, false)
        )

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: TopViewHolder, position: Int) {
        holder.bind(cinemaData[position], adultContent)
    }

    override fun getItemCount(): Int = cinemaData.size

    inner class TopViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(cinemaDTO: CinemaDTO.CinemaPreview, adult: Boolean) {
            if (adult)
                showContent(itemView, cinemaDTO)
            else
                showAdultContent(itemView, cinemaDTO)
        }
    }

    private fun showAdultContent(itemView: View, cinemaDTO: CinemaDTO.CinemaPreview) {
        if (!cinemaDTO.adult) {
            itemView.apply {
                findViewById<TextView>(R.id.searchItemName).text = cinemaDTO.title
                findViewById<TextView>(R.id.searchItemYear).text =
                    cinemaDTO.release_date.substringBefore("-")
                findViewById<TextView>(R.id.searchItemRating).text =
                    cinemaDTO.vote_average.toString()
                setOnClickListener {
                    onItemViewClickListener?.onItemViewClick(cinemaDTO)
                }
            }
        } else itemView.visibility = View.GONE
    }

    private fun showContent(itemView: View, cinemaDTO: CinemaDTO.CinemaPreview) {
        itemView.apply {
            findViewById<TextView>(R.id.searchItemName).text = cinemaDTO.title
            findViewById<TextView>(R.id.searchItemYear).text =
                cinemaDTO.release_date.substringBefore("-")
            findViewById<TextView>(R.id.searchItemRating).text =
                cinemaDTO.vote_average.toString()
            setOnClickListener {
                onItemViewClickListener?.onItemViewClick(cinemaDTO)
            }
        }
    }


}