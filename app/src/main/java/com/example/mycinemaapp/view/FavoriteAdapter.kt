package com.example.mycinemaapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mycinemaapp.R
import com.example.mycinemaapp.model.Cinema

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.RecyclerItemViewHolder>() {

    private var data: List<Cinema> = arrayListOf()

    fun setData(data: List<Cinema>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteAdapter.RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_favorite_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: Cinema) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.apply {
                    findViewById<TextView>(R.id.favoriteItemName).text = data.film
                    findViewById<TextView>(R.id.favoriteItemYear).text =
                        data.release.substringBefore("-")
                    findViewById<TextView>(R.id.favoriteItemRating).text = data.average.toString()
                    findViewById<TextView>(R.id.favoriteItemNote).text = data.note
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