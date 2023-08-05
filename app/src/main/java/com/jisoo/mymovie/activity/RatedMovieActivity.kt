package com.jisoo.mymovie.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jisoo.mymovie.adapter.AdapterMovies
import com.jisoo.mymovie.databinding.ActivityRatedMovieBinding
import com.jisoo.mymovie.utils.EnumClassSortBy
import com.jisoo.mymovie.utils.PrefsAuthentikasi
import com.jisoo.mymovie.viewModel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RatedMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRatedMovieBinding
    private val movieViewModel by viewModels<MovieViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatedMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMovie.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val isLogin = PrefsAuthentikasi(this).isLogin()
        if (isLogin == true){
            val sessionId = PrefsAuthentikasi(this).getSessionId()
            if (sessionId != null){
                getRatedMoviesBySessionId(sessionId)
            }
        }else{
            val guestSessionId = PrefsAuthentikasi(this).getGuestSessionId()
            if (guestSessionId != null){
                geRatedMoviesByGuest(guestSessionId)
            }
        }
    }

    private fun geRatedMoviesByGuest(guestSessionId: String) {
        val adapterMovies = AdapterMovies()
        movieViewModel.getRatedMovieByGuest(guestSessionId).observe(this){ rated->
            if (rated != null){
                adapterMovies.submitList(rated)
                binding.rvMovies.apply {
                    adapter = adapterMovies
                    layoutManager = LinearLayoutManager(this@RatedMovieActivity)
                }
            }
        }
    }

    private fun getRatedMoviesBySessionId(sessionId: String) {
        val adapterMovies = AdapterMovies()
        movieViewModel.getRatedMovieBySessionId(sessionId, sessionId).observe(this){ rated->
            if (rated != null){
                adapterMovies.submitList(rated)
                binding.rvMovies.apply {
                    adapter = adapterMovies
                    layoutManager = LinearLayoutManager(this@RatedMovieActivity)
                }
            }
        }
    }
}