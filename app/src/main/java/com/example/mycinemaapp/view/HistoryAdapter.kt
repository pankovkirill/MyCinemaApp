package com.example.mycinemaapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mycinemaapp.R
import com.example.mycinemaapp.databinding.RecyclerItemBinding
import com.example.mycinemaapp.model.Cinema
import com.example.mycinemaapp.model.CinemaDTO
import com.example.mycinemaapp.model.CinemaDetailsDTO

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.RecyclerItemViewHolder>() {

    private var data: List<Cinema> = arrayListOf()

    fun setData(data: List<Cinema>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAdapter.RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_search_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: HistoryAdapter.RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: Cinema) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.apply {
                    findViewById<TextView>(R.id.searchItemName).text = data.film
                    findViewById<TextView>(R.id.searchItemYear).text =
                        data.release.substringBefore("-")
                    findViewById<TextView>(R.id.searchItemRating).text = data.average.toString()
                    findViewById<TextView>(R.id.searchItemNote).text = data.note
                }
                itemView.setOnClickListener {
                    Toast.makeText(
                        itemView.context,
                        "${data.film}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }
}