    package com.example.ratefilm.ui.activitys

    import android.content.Intent
    import android.os.Bundle
    import android.view.View
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.ViewCompat
    import androidx.core.view.WindowInsetsCompat
    import androidx.lifecycle.ViewModelProvider
    import androidx.lifecycle.lifecycleScope
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.bumptech.glide.Glide
    import com.example.ratefilm.R
    import com.example.ratefilm.adapter.ReviewAdapter
    import com.example.ratefilm.adapter.UpcomingAdapter
    import com.example.ratefilm.databinding.ActivityDetailBinding
    import com.example.ratefilm.db.MovieDatabase
    import com.example.ratefilm.repository.MovieRepository
    import com.example.ratefilm.ui.MovieViewModel
    import com.example.ratefilm.ui.MovieViewModelProviderFactory
    import com.example.ratefilm.ui.fragments.HomeFragment
    import kotlinx.coroutines.launch
    import kotlin.properties.Delegates

    class DetailActivity : AppCompatActivity() {
        private lateinit var binding: ActivityDetailBinding
        private lateinit var movieViewModel: MovieViewModel
        private var movieId by Delegates.notNull<Int>()
        private var reviewAdapter: ReviewAdapter = ReviewAdapter()
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityDetailBinding.inflate(layoutInflater)
            val movieRepository = MovieRepository(MovieDatabase(this))
            val viewModelProviderFactory = MovieViewModelProviderFactory(application,movieRepository)
            movieViewModel = ViewModelProvider(this,viewModelProviderFactory).get(MovieViewModel::class.java)
            setContentView(binding.root)
            movieId = intent.getIntExtra("movie_id", -1)
            LoadDetail()
            BackHome()




        }

        private fun BackHome() {
            binding.detailBack.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish() // kết thúc DetailActivity
            }
        }

        fun LoadDetail() {

            binding.detailActivityProgressbar.visibility = View.VISIBLE // 👈 Show progress
            binding.detailActivityLayout.visibility = View.GONE
            lifecycleScope.launch {
                val detail = movieViewModel.getDetail(movieId)
                val credit = movieViewModel.getCredit(movieId)

                if (credit != null && detail != null) {
                    val director = credit.crew.firstOrNull { it.job == "Director" }
                    val mainActors = credit.cast.take(5)
                    val actorNames = mainActors.joinToString(separator = ", ") { it.name } + ",..."

                    binding.detailDirector.text = director?.name
                    binding.detailActors.text = actorNames
                    binding.detailTitle.text = detail.title
                    binding.detailDuration.text = "${detail.runtime}'"
                    binding.detailStar.text = (detail.vote_average / 2).toString()
                    binding.detailGenres.text = detail.genres.joinToString(", ") { it.name.replace("Phim ", "") }

                    val imageURL = "https://image.tmdb.org/t/p/w342/" + detail.poster_path
                    Glide.with(this@DetailActivity)
                        .load(imageURL)
                        .into(binding.detailPoster)
                }

                binding.detailActivityProgressbar.visibility = View.GONE
                binding.detailActivityLayout.visibility = View.VISIBLE
                LoadReview()
                SetupRecyclerReview()
            }
        }

        fun LoadReview(){
            lifecycleScope.launch {
                val reviews = movieViewModel.getReview(movieId)
                reviewAdapter.differ.submitList(reviews?.results?.toList())
            }
        }

        fun SetupRecyclerReview(){
            binding.detailReviewLayout.apply {
                adapter = reviewAdapter
                layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

            }
        }

    }