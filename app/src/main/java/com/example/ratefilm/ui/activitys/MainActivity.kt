package com.example.ratefilm.ui.activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.ratefilm.R
import com.example.ratefilm.databinding.ActivityMainBinding
import com.example.ratefilm.db.MovieDatabase
import com.example.ratefilm.repository.MovieRepository
import com.example.ratefilm.ui.MovieViewModel
import com.example.ratefilm.ui.MovieViewModelProviderFactory

class MainActivity : AppCompatActivity() {
    lateinit var movieViewModel: MovieViewModel

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movieRepository = MovieRepository(MovieDatabase(this))
        val viewModelProviderFactory = MovieViewModelProviderFactory(application,movieRepository)
        movieViewModel = ViewModelProvider(this,viewModelProviderFactory).get(MovieViewModel::class.java)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

    }
}