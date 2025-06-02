package com.example.ratefilm.model

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    val id: Int,
    val page: Int,
    @SerializedName("results")
    val results: List<Review>,
    val total_pages: Int,
    val total_results: Int
)