package com.example.ratefilm.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ratefilm.R
import com.example.ratefilm.model.Review
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewItemHolder>() {

    inner class ReviewItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    private val differCallback = object : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewItemHolder {
        return ReviewItemHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ReviewItemHolder, position: Int) {
        val review = differ.currentList[position]
        val name = holder.itemView.findViewById<TextView>(R.id.item_review_name)
        val avatar = holder.itemView.findViewById<ImageView>(R.id.item_review_avatar)
        val rating = holder.itemView.findViewById<TextView>(R.id.item_review_rating_score)
        val content = holder.itemView.findViewById<TextView>(R.id.item_review_content)
        val date = holder.itemView.findViewById<TextView>(R.id.item_review_date_created)

        holder.itemView.apply {
            name.text = review.author
            if (review.author_details.avatar_path != null){
                val imageURL = "https://image.tmdb.org/t/p/w342/"+review.author_details.avatar_path

                Glide.with(holder.itemView.context)
                    .load(imageURL)
                    .into(avatar)
            }

            rating.text = review.author_details.rating +"/10"
            content.text = review.content

            val parts = review.created_at.split("T")
            val datePart = parts[0]
            date.text = datePart
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}