package com.example.ratefilm.model

data class MovieResponse(
    val dates: Dates?,
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)

data class Dates(
    val maximum: String
)
