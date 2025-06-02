package com.example.ratefilm.model

data class Review(
    val author: String,
    val author_details: AuthorDetails,
    val content: String,
    val created_at: String,
    val id: String,
)