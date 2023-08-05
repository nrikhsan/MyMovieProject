package com.jisoo.mymovie.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.jisoo.mymovie.R
import com.jisoo.mymovie.adapter.AdapterPersonalListMovie
import com.jisoo.mymovie.databinding.ActivityDetailPersonalListBinding

import com.jisoo.mymovie.response.PersonalListMovies
import com.jisoo.mymovie.viewModel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailPersonalListActivity : AppCompatActivity(){

    private lateinit var binding: ActivityDetailPersonalListBinding

    companion object{
        const val LIST_ID = "list_id"
    }

    private val movieViewModel by viewModels<MovieViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPersonalListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.toolbarMovie.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        val extras = intent?.extras
        if (extras != null){
            val listId = extras.getString(LIST_ID)
            if (listId != null){

                showDetailPersonalList(listId)
            }
        }
    }

    private fun showDetailPersonalList(listId: String) {
        movieViewModel.getDetailPersonalList(listId).observe(this){ detail->
            if (detail != null){
                binding.apply {
                    tvNameCollection.text = detail.name
                    tvCreated.text = detail.createdBy

                    val count = detail.itemCount
                    if (count > 0){
                        tvItemCount.setTextColor(ContextCompat.getColor(this@DetailPersonalListActivity, R.color.dark_purple_grey))
                        tvItemCount.text = count.toString()
                    }else{
                        tvItemCount.setTextColor(ContextCompat.getColor(this@DetailPersonalListActivity, R.color.red))
                        tvItemCount.text = count.toString()
                    }

                    //get list movies personal list
                    showListMoviesPersonalList(detail.items)

                    binding.btnFindMovies.setOnClickListener {
                        val intent = Intent(this@DetailPersonalListActivity, SearchMoviePersonalListActivity::class.java)
                        intent.putExtra(SearchMoviePersonalListActivity.LIST_ID, detail.id)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    //get list movies personal list
    private fun showListMoviesPersonalList(items: List<PersonalListMovies>) {
        val adapterPersonalList = AdapterPersonalListMovie()
        adapterPersonalList.submitList(items)
        binding.rvMoviePersonalList.apply {
            adapter = adapterPersonalList
            layoutManager = LinearLayoutManager(this@DetailPersonalListActivity)
        }
    }
}