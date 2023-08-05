package com.jisoo.mymovie.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.jisoo.mymovie.R
import com.jisoo.mymovie.databinding.ActivityDetailMovieBinding
import com.jisoo.mymovie.databinding.BottomSheetAddRatingBinding
import com.jisoo.mymovie.response.RatingRequest
import com.jisoo.mymovie.utils.Constants.URL_IMAGE_BACKDROP
import com.jisoo.mymovie.utils.Constants.URL_IMAGE_POSTER
import com.jisoo.mymovie.utils.PrefsAuthentikasi
import com.jisoo.mymovie.utils.PrefsRating
import com.jisoo.mymovie.viewModel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMovieBinding
    private val movieViewModel by viewModels<MovieViewModel>()
    private lateinit var bottomSheetDialog: BottomSheetDialog
    companion object{
        const val MOVIE_ID = "movie_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMovie.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val extras = intent?.extras
        if (extras != null){
            val movieId = extras.getString(MOVIE_ID)
            if (movieId != null){

                //set detail movie
                setDetailMovie(movieId)

            }
        }
    }

    //set detail movie
    private fun setDetailMovie(movieId: String) {
        movieViewModel.getDetailMovie(movieId).observe(this){ detail->
            if (detail != null){
                binding.apply {

                    toolbarMovie.title = detail.originalTitle

                    Glide.with(this@DetailMovieActivity)
                        .load(URL_IMAGE_BACKDROP + detail.backdropPath)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgBackdrop)

                    Glide.with(this@DetailMovieActivity)
                        .load(URL_IMAGE_POSTER + detail.posterPath)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transform(RoundedCorners(20))
                        .into(imgPoster)

                    tvTitle.text = detail.title
                    val genres = detail.genres.joinToString { it.name }
                    tvGenres.text = genres

                    if (detail.tagline.isNotEmpty()){
                        tvTagline.text = detail.tagline
                    }else{
                        tvTagline.text = getString(R.string.tidak_ada_tagline)
                    }
                    if (detail.overview.isNotEmpty()) {
                        tvOverview.text = detail.overview
                    }else{
                        tvOverview.text = getString(R.string.deskripsi_tidak_tersedia)
                    }

                    val rating = PrefsRating(this@DetailMovieActivity).getRating(detail.id)

                    binding.btnAddRating.apply {
                        text = rating.toString()
                        setOnClickListener {
                            showBottomSheetRating(detail.id)
                        }
                    }
                }
            }
        }
    }

    //memberi rating movie
    private fun showBottomSheetRating(movieId: String) {
        val bottomSheetAddRatingBinding = BottomSheetAddRatingBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetAddRatingBinding.root)
        bottomSheetDialog.show()

        val rating = PrefsRating(this).getRating(movieId)

        bottomSheetAddRatingBinding.ratingBarMovie.rating = rating!!.toFloat()

        //add rating
        bottomSheetAddRatingBinding.btnSubmitRating.setOnClickListener {
            val prefsAuthentikasi = PrefsAuthentikasi(this)
            val value = bottomSheetAddRatingBinding.ratingBarMovie.rating.toDouble()
            val data = RatingRequest(value)

            if(prefsAuthentikasi.isLogin() == true){
                if (prefsAuthentikasi.getSessionId() != null){
                    movieViewModel.addRatingMovie(movieId, prefsAuthentikasi.getSessionId().toString(), data).observe(this){ ratingMovie->
                        if (ratingMovie != null){
                            binding.btnAddRating.text = data.value.toString()
                            Snackbar.make(binding.root, ratingMovie.statusMessage + data.value, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            bottomSheetDialog.dismiss()
        }

        //delete rating
        bottomSheetAddRatingBinding.btnDeleteRating.setOnClickListener {
            val prefsAuthentikasi = PrefsAuthentikasi(this)
            val prefsRatingMovie = PrefsRating(this)
            if(prefsAuthentikasi.isLogin() == true){
                if (prefsAuthentikasi.getSessionId() != null){
                    movieViewModel.deleteRating(movieId, prefsAuthentikasi.getSessionId().toString()).observe(this){ ratingMovie->
                        if (ratingMovie != null){
                            Snackbar.make(binding.root, ratingMovie.statusMessage, Snackbar.LENGTH_SHORT).show()
                            binding.btnAddRating.text = 0.0f.toString()
                            prefsRatingMovie.removeData(movieId)
                        }
                    }
                }
            }
            bottomSheetDialog.dismiss()
        }
    }
}