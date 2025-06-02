package com.example.ratefilm.api

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.ratefilm.constants.Constants.Companion.API_KEY
import com.example.ratefilm.model.CreditResponse
import com.example.ratefilm.model.MovieResponse
import com.example.ratefilm.model.Result
import com.example.ratefilm.model.ReviewResponse
import retrofit2.Response
import retrofit2.http.Path

interface MovieAPI {

    @GET("movie/popular")
    suspend fun getPopular(
        @Query("language")
        language: String = "vi",
        @Query("page")
        page :Int = 1,
        @Query("api_key")
        api: String = API_KEY
    ): Response<MovieResponse>

    @GET("movie/upcoming")
    suspend fun getUpcoming(
        @Query("language") language: String = "vi",
        @Query("page") page: Int = 1,
        @Query("api_key") api: String = API_KEY
    ): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("language") language: String = "vi",
        @Query("page") page: Int = 1,
        @Query("api_key") api: String = API_KEY
    ): Response<MovieResponse>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("language") language: String = "vi",
        @Query("page") page: Int = 1,
        @Query("api_key") api: String = API_KEY
    ): Response<MovieResponse>

    @GET("movie/{movieId}/credits")
    suspend fun getMovieCredits(
        @Path("movieId") movieId: Int,
        @Query("language") language: String = "vi",
        @Query("api_key") apiKey: String = API_KEY
    ): Response<CreditResponse>

    @GET("movie/{movieId}")
    suspend fun getDetail(
        @Path("movieId") movieId: Int,
        @Query("language") language: String = "vi",
        @Query("api_key") apiKey: String = API_KEY
    ): Result

    @GET("movie/{movieId}/reviews")
    suspend fun getMovieReviews(
        @Path("movieId") movieId: Int,
        @Query("language") language: String = "en",
        @Query("api_key") apiKey: String = API_KEY
    ): Response<ReviewResponse>


}