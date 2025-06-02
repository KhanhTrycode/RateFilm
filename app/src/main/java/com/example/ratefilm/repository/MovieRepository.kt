package com.example.ratefilm.repository

import com.example.ratefilm.api.RetrofitInstance
import com.example.ratefilm.db.MovieDatabase
import com.example.ratefilm.model.Result

class MovieRepository(val db: MovieDatabase) {
    suspend fun getPopular(language: String,pageNumber: Int) = RetrofitInstance.api.getPopular(language,pageNumber)

    suspend fun getUpcoming(language: String,pageNumber: Int) = RetrofitInstance.api.getUpcoming(language,pageNumber)

    suspend fun getToprated(language: String,pageNumber: Int) = RetrofitInstance.api.getTopRated(language,pageNumber)

    suspend fun searchMovies(searchQuery: String,language: String, pageNumber: Int) = RetrofitInstance.api.searchMovies(searchQuery,language,pageNumber)

    suspend fun getCredit(searchQuery: Int) = RetrofitInstance.api.getMovieCredits(searchQuery)

    suspend fun getReview(searchQuery: Int) = RetrofitInstance.api.getMovieReviews(searchQuery)

    suspend fun getDetail(searchQuery: Int) = RetrofitInstance.api.getDetail(searchQuery)

    suspend fun upsert(result: Result) = db.getMovieDAO().upsert(result)

    fun getFavoriteMovie() = db.getMovieDAO().getAllMovie()

    suspend fun deleteMovie(result: Result) = db.getMovieDAO().deleteMovie(result)
}