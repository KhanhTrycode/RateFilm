package com.example.ratefilm.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.viewpager2.widget.ViewPager2
import com.example.ratefilm.R
import com.example.ratefilm.adapter.BaseMovieAdapter
import com.example.ratefilm.adapter.ImageSliderAdapter
import com.example.ratefilm.adapter.PopularAdapter
import com.example.ratefilm.adapter.TopRateAdapter
import com.example.ratefilm.adapter.UpcomingAdapter
import com.example.ratefilm.databinding.FragmentHomeBinding
import com.example.ratefilm.model.MovieResponse
import com.example.ratefilm.repository.MovieRepository
import com.example.ratefilm.ui.MovieViewModel
import com.example.ratefilm.ui.activitys.MainActivity
import com.example.ratefilm.util.Resource
import kotlin.math.abs

class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var movieViewModel: MovieViewModel
    lateinit var popularAdapter: PopularAdapter
    lateinit var upcomingAdapter: UpcomingAdapter
    lateinit var topRateAdapter: TopRateAdapter
    lateinit var retryButton: Button
    lateinit var errorText: TextView
    lateinit var itemHeadlineError: CardView
    lateinit var binding: FragmentHomeBinding
    private lateinit var sliderAdapter: ImageSliderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        movieViewModel = (activity as MainActivity).movieViewModel
//        setupPopularRecycler()
        setupUpcomingRecycler()
        setupTopRatedRecycler()

//        movieViewModel.popularMovies.observe(viewLifecycleOwner) { response ->
//            when (response) {
//                is Resource.Success<*> -> {
//                    response.data?.let { movieResponse ->
//                        val limitedMovies = movieResponse.results.take(10)
//                        popularAdapter.differ.submitList(limitedMovies)
//                    }
//                }
//
//             else-> {}
//            }
//        }
        MovieObserver(movieViewModel.upcomingMovies, upcomingAdapter)
        MovieObserver(movieViewModel.topMovies, topRateAdapter)
        setupImageSlider()



    }

    private fun setupImageSlider() {
        // Danh sách ảnh ví dụ
        val imageUrls = listOf(
            "https://image.tmdb.org/t/p/w500//ybBIIzDL1B9yH8OVFav81JTZmoN.jpg",

            "https://image.tmdb.org/t/p/w500//fTrQsdMS2MUw00RnzH0r3JWHhts.jpg","https://image.tmdb.org/t/p/w500//ybBIIzDL1B9yH8OVFav81JTZmoN.jpg",
            "https://image.tmdb.org/t/p/w500//fTrQsdMS2MUw00RnzH0r3JWHhts.jpg","https://image.tmdb.org/t/p/w500//ybBIIzDL1B9yH8OVFav81JTZmoN.jpg",
            "https://image.tmdb.org/t/p/w500//fTrQsdMS2MUw00RnzH0r3JWHhts.jpg","https://image.tmdb.org/t/p/w500//ybBIIzDL1B9yH8OVFav81JTZmoN.jpg",
            "https://image.tmdb.org/t/p/w500//fTrQsdMS2MUw00RnzH0r3JWHhts.jpg","https://image.tmdb.org/t/p/w500//ybBIIzDL1B9yH8OVFav81JTZmoN.jpg"
        )

        // Tạo adapter
        sliderAdapter = ImageSliderAdapter(imageUrls)

        // Gán adapter cho ViewPager2
        binding.imageSlider.adapter = sliderAdapter

        // Hiệu ứng mờ ảnh kế tiếp
        binding.imageSlider.offscreenPageLimit = 10
        val pageMargin = 20
        val pageOffset = 30

        binding.imageSlider.setPageTransformer { page, position ->
            val offset = position * -(2 * pageOffset + pageMargin)
            page.translationX = offset
            page.alpha = 0.25f + (1 - abs(position)) * 0.75f
            page.scaleY = 0.85f + (1 - abs(position)) * 0.15f
        }
    }

    private fun MovieObserver(
        movies: MutableLiveData<Resource<MovieResponse>>,
        adapter: BaseMovieAdapter
    ) {
        movies.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success<*> -> {
                    response.data?.let { movieResponse ->
                        if (movieResponse.results?.isNotEmpty() == true) {
                            adapter.differ.submitList(movieResponse.results.toList())
                        } else {
                            Toast.makeText(context, "No movies available", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                is Resource.Error<*> -> {

                    response.message?.let { message ->
                        Log.e("Error", "Error message: $message")
                        Toast.makeText(activity, "Sorry error: $message", Toast.LENGTH_SHORT).show()

                    }
                }

                is Resource.Loading<*> -> {
                    Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun hideErrorMessage() {

    }

    private fun hideProgressBar() {

    }

//    private fun setupPopularRecycler(){
//        popularAdapter = PopularAdapter()
//        binding.recyclerPopular.apply {
//            adapter = popularAdapter
//            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
//
//        }
//    }

    private fun setupUpcomingRecycler() {
        upcomingAdapter = UpcomingAdapter()
        binding.recyclerUpcoming.apply {
            adapter = upcomingAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        }
    }

    private fun setupTopRatedRecycler() {
        topRateAdapter = TopRateAdapter()
        binding.recyclertoprated.apply {
            adapter = topRateAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        }
    }


}