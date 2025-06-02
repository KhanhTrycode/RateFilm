package com.example.ratefilm.adapter

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.ratefilm.R

class TopRateAdapter: BaseMovieAdapter() {
    override fun onBindViewHolder(holder: MovieItemHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val movie = differ.currentList[position]

        val movieTitle = holder.itemView.findViewById<TextView>(R.id.textTitle)
        val popularImage = holder.itemView.findViewById<ImageView>(R.id.imagePopular)

        holder.itemView.apply {
            movieTitle.text = movie.title

            val imageURL = "https://image.tmdb.org/t/p/w342/"+movie.poster_path
            Glide.with(holder.itemView.context)
                .load(imageURL)
                .into(popularImage)
        }
    }
}