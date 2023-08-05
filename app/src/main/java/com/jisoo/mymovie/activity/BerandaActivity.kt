package com.jisoo.mymovie.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jisoo.mymovie.R
import com.jisoo.mymovie.adapter.AdapterMovies
import com.jisoo.mymovie.databinding.ActivityBerandaBinding
import com.jisoo.mymovie.databinding.BottomSheetSortByBinding
import com.jisoo.mymovie.utils.Constants.URL_IMAGE_PROFILE
import com.jisoo.mymovie.utils.PrefsAuthentikasi
import com.jisoo.mymovie.viewModel.LoginViewModel
import com.jisoo.mymovie.viewModel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BerandaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBerandaBinding
    private val loginViewModel by viewModels<LoginViewModel>()
    private val movieViewModel by viewModels<MovieViewModel>()
    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerandaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //mengambil data film
        getDiscoverMovie()

        val prefsAuthentikasi = PrefsAuthentikasi(this)
        if (prefsAuthentikasi.isLogin() == true) {

            //mengambil data user
            binding.imgProfile.setOnClickListener {
                startActivity(Intent(this, AkunActivity::class.java))
            }
            setDataUser()
        }else{
            binding.tvUsername.text = resources.getString(R.string.kamu_datang_sebagai_tamu)
            binding.imgProfile.setOnClickListener {
                startActivity(Intent(this, AkunActivity::class.java))
            }
        }

        binding.toolbarMovie.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_sort_by -> {
                    showBottomSheet()
                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    //sort by
    private fun showBottomSheet() {
        val bottomSheetSortByBinding = BottomSheetSortByBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetSortByBinding.root)
        bottomSheetDialog.show()

        bottomSheetSortByBinding.toolbarMovie.setNavigationOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }

    //get movie
    private fun getDiscoverMovie() {
        val adapterMovies = AdapterMovies()
        movieViewModel.discover.observe(this){ discover->
            if (discover != null){
                adapterMovies.submitList(discover)
                binding.rvMovies.apply {
                    adapter = adapterMovies
                    layoutManager = LinearLayoutManager(this@BerandaActivity)

                }
            }
        }
    }

    private fun setDataUser() {
        val sessionId = PrefsAuthentikasi(this).getSessionId()
        if (sessionId != null) {

            setUsername(sessionId)
        }
    }

    private fun setUsername(sessionId: String) {
        loginViewModel.getAccountDetail(sessionId, sessionId).observe(this) { detail ->
            if (detail != null) {
                binding.tvUsername.text = detail.username

                Glide.with(this)
                    .load(URL_IMAGE_PROFILE)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.imgProfile)
            }
        }
    }
}