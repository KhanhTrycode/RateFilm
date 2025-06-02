package com.example.ratefilm.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ratefilm.model.CreditResponse
import com.example.ratefilm.model.Result
import com.example.ratefilm.model.MovieResponse
import com.example.ratefilm.model.Review
import com.example.ratefilm.model.ReviewResponse
import com.example.ratefilm.repository.MovieRepository
import com.example.ratefilm.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MovieViewModel(app: Application, val movieRepository: MovieRepository) :
    AndroidViewModel(app) {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    val popularMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val upcomingMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val topMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val movieCredits: MutableLiveData<Resource<CreditResponse>> = MutableLiveData()
    var PopularPage = 1
    var topRatedPage = 1
    var upcomingPage = 1
    var popularResponse: MovieResponse? = null
    var upcomingResponse: MovieResponse? = null
    var topRatedResponse: MovieResponse? = null
    var reviewResponse: ReviewResponse? = null
    var reviewPage = 1
    val searchMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var searchPage = 1
    var searchMovieResponse: MovieResponse? = null
    var oldSearchQuery: String? = null
    var newSearchQuery: String? = null

    init {
        getMovies("vi")
    }

    fun getMovies(countryCode: String) = viewModelScope.launch {
        popularMovies.postValue(Resource.Loading())
        upcomingMovies.postValue(Resource.Loading())
        topMovies.postValue(Resource.Loading())

        val popularDeferred = async { movieRepository.getPopular(countryCode, PopularPage) }
        val upcomingDeferred = async { movieRepository.getUpcoming(countryCode, upcomingPage) }
        val topRatedDeferred = async { movieRepository.getToprated(countryCode, topRatedPage) }

        val popularResponseResult = popularDeferred.await()
        val upcomingResponseResult = upcomingDeferred.await()
        val topRatedResponseResult = topRatedDeferred.await()

        // Cập nhật biến lưu response với handleMovieResponse để đảm bảo logic load trang
        popularMovies.postValue(handleMovieResponse(popularResponseResult, "popular"))
        upcomingMovies.postValue(handleMovieResponse(upcomingResponseResult, "upcoming"))
        topMovies.postValue(handleMovieResponse(topRatedResponseResult, "toprated"))
    }

    fun searchMovies(searchQuery: String) = viewModelScope.launch {
        searchMoviesInternet(searchQuery)
    }

    private fun handleMovieResponse(
        response: Response<MovieResponse>,
        type: String
    ): Resource<MovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                when (type) {
                    "popular" -> {
                        if (popularResponse == null) {
                            popularResponse = resultResponse
                        }
                        return Resource.Success(popularResponse!!)
                    }

                    "upcoming" -> {
                        upcomingPage++
                        upcomingResponse = resultResponse
                        return Resource.Success(upcomingResponse!!)
                    }

                    "toprated" -> {
                        topRatedPage++
                        if (topRatedResponse == null) {
                            topRatedResponse = resultResponse
                        } else {
                            topRatedResponse = resultResponse
                        }
                        return Resource.Success(topRatedResponse!!)
                    }

                    else -> {}
                }
            }
        }
        return Resource.Error("Error: ${response.code()} - ${response.message()}")
    }


    private fun handleSearchResponse(response: Response<MovieResponse>): Resource<MovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchPage++
                if (searchMovieResponse == null || oldSearchQuery != newSearchQuery) {
                    oldSearchQuery = newSearchQuery
                    searchMovieResponse = resultResponse
                } else {
                    val oldMovies = searchMovieResponse?.results
                    val newMovies = resultResponse.results

                }
                return Resource.Success(searchMovieResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    fun addToFavorite(result: Result) = viewModelScope.launch {
        movieRepository.upsert(result)
    }

    fun getFavorite() = movieRepository.getFavoriteMovie()
    fun deleteMovie(article: Result) = viewModelScope.launch {
        movieRepository.deleteMovie(article)
    }

    fun internetConnect(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    private suspend fun movieInternet(
        countryCode: String,
        funcGet: Response<MovieResponse>,
        movies: MutableLiveData<Resource<MovieResponse>>,
        type: String
    ) {
        movies.postValue(Resource.Loading())
        try {
            if (internetConnect(this.getApplication())) {
                val response = funcGet
                if (response.isSuccessful) {
                    movies.postValue(handleMovieResponse(response, type))
                } else {
                    movies.postValue(Resource.Error("API Error: ${response.code()} - ${response.message()}"))
                }
            } else {
                movies.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            Log.e("MovieViewModel", "Error: ${t.message}")
            when (t) {
                is IOException -> movies.postValue(Resource.Error("Unable to connect"))
                else -> movies.postValue(Resource.Error("No signal"))
            }
        }
    }


    private suspend fun searchMoviesInternet(searchQuery: String) {
        newSearchQuery = searchQuery
        searchMovies.postValue(Resource.Loading())
        try {
            if (internetConnect(this.getApplication())) {
                val response = movieRepository.searchMovies(searchQuery, "vi", searchPage)
                searchMovies.postValue(handleSearchResponse(response))
            } else {
                searchMovies.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchMovies.postValue(Resource.Error("Unable to connect"))
                else -> searchMovies.postValue(Resource.Error("No signal"))
            }
        }
    }

    suspend fun getCredit(movieId: Int): CreditResponse? {
        return if (internetConnect(getApplication())) {
            val response = movieRepository.getCredit(movieId)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } else {
            null
        }
    }

    suspend fun getDetail(movieId: Int): Result? {
        return if (internetConnect(getApplication())) {
            movieRepository.getDetail(movieId)
        } else {
            null
        }

    }

    suspend fun getReview(movieId: Int): ReviewResponse? {
        return if (internetConnect(getApplication())) {
            val response = movieRepository.getReview(movieId)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } else {
            null
        }


    }
}