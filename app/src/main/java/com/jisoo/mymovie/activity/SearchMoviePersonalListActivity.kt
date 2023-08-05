package com.jisoo.mymovie.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jisoo.mymovie.adapter.AdapterAddMoviesToPersonalList
import com.jisoo.mymovie.databinding.ActivitySearchPersonalListBinding
import com.jisoo.mymovie.response.MovieId
import com.jisoo.mymovie.utils.PrefsAuthentikasi
import com.jisoo.mymovie.viewModel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMoviePersonalListActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivitySearchPersonalListBinding
    private val movieViewModel by viewModels<MovieViewModel>()
    companion object{
        const val LIST_ID = "list_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchPersonalListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchMovie.onActionViewExpanded()
        binding.searchMovie.setOnQueryTextListener(this)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null){
            movieViewModel.searchMovieAndAddToPersonalList(query).observe(this){ list->
                val adapterMovies = AdapterAddMoviesToPersonalList()
                adapterMovies.submitList(list?.results)
                binding.apply {
                    rvMovies.adapter = adapterMovies
                    rvMovies.layoutManager = LinearLayoutManager(this@SearchMoviePersonalListActivity)
                }

                val sessionId = PrefsAuthentikasi(this).getSessionId()
                val extras = intent?.extras
                if (extras != null){
                    val listId = extras.getString(LIST_ID)
                    if (listId != null){
                        adapterMovies.addTo(object : AdapterAddMoviesToPersonalList.AddTo{
                            override fun addToPersonalList(movieId: String) {
                                val mediaId = MovieId(movieId)
                                movieViewModel.addMovieToPersonalList(listId, sessionId.toString(), mediaId).observe(this@SearchMoviePersonalListActivity){
                                    Snackbar.make(binding.root, it?.statusMessage.toString(), Snackbar.LENGTH_SHORT).show()
                                }
                            }
                        })
                    }
                }
            }
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }
}