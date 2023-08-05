package com.jisoo.mymovie.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.jisoo.mymovie.R
import com.jisoo.mymovie.adapter.AdapterPersonalList
import com.jisoo.mymovie.databinding.ActivityAkunBinding
import com.jisoo.mymovie.databinding.BottomSheetPersonalListBinding
import com.jisoo.mymovie.response.RequestCreateList
import com.jisoo.mymovie.utils.Constants.URL_IMAGE_PROFILE
import com.jisoo.mymovie.utils.PrefsAuthentikasi
import com.jisoo.mymovie.viewModel.LoginViewModel
import com.jisoo.mymovie.viewModel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AkunActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAkunBinding
    private val loginViewModel by viewModels<LoginViewModel>()
    private val movieViewModel by viewModels<MovieViewModel>()
    private lateinit var bottomSheet: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefsAuthentikasi = PrefsAuthentikasi(this)

        binding.toolbarMovie.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnRated.setOnClickListener {
            startActivity(Intent(this, RatedMovieActivity::class.java))
        }

        //logout
        binding.btnLogout.setOnClickListener {
            prefsAuthentikasi.removeData()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        if (prefsAuthentikasi.isLogin() == true) {

            //mengambil data user
            setDataUser()

            //mengambil personal list
            val sessionId = prefsAuthentikasi.getSessionId()
            getPersonalList(sessionId)

            //tampilkan bottom sheet dialog personal list
            binding.btnList.setOnClickListener {
                showBottomSheetPersonalList()
            }
        }else{
            binding.btnLogout.isVisible = false
            binding.tvUsername.text = resources.getString(R.string.kamu_datang_sebagai_tamu)
            binding.rvPersonalList.isVisible = false
            binding.btnList.setOnClickListener {
                Snackbar.make(binding.root, R.string.kamu_datang_sebagai_tamu, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    //mengambil personal list
    private fun getPersonalList(sessionId: String?) {
        val adapterPersonalList = AdapterPersonalList()
        if (sessionId != null){
            movieViewModel.getPersonalList(sessionId, sessionId).observe(this){ list->
                if (list != null){
                    adapterPersonalList.submitList(list)
                    binding.rvPersonalList.apply {
                        adapter = adapterPersonalList
                        layoutManager = LinearLayoutManager(this@AkunActivity)
                        isNestedScrollingEnabled = true
                        hasNestedScrollingParent()

                        adapterPersonalList.onClick(object : AdapterPersonalList.OnClicked{
                            override fun deleteList(listId: String) {
                                movieViewModel.deletedList(listId, sessionId).observe(this@AkunActivity){ delete->
                                    Snackbar.make(binding.root, "${delete?.statusMessage}", Snackbar.LENGTH_SHORT).show()
                                }
                            }
                        })
                    }
                }
            }
        }
    }


    //bottom Sheet personal list
    private fun showBottomSheetPersonalList() {
        val bottomSheetPersonalListBinding = BottomSheetPersonalListBinding.inflate(layoutInflater)
        bottomSheet = BottomSheetDialog(this)
        bottomSheet.setContentView(bottomSheetPersonalListBinding.root)
        bottomSheet.show()

        bottomSheetPersonalListBinding.toolbarMovie.setNavigationOnClickListener {
            bottomSheet.dismiss()
        }

        bottomSheetPersonalListBinding.btnAddPersonalList.setOnClickListener {
            val nameList = bottomSheetPersonalListBinding.edtListName.text.toString()
            val deskripsi = bottomSheetPersonalListBinding.edtDeskripsi.text.toString()
            val data = RequestCreateList(nameList, deskripsi)
            val sessionId = PrefsAuthentikasi(this).getSessionId()

            //membuat list
            if (sessionId != null){
                movieViewModel.createList(sessionId, data).observe(this){ list->
                    if (list != null){
                        bottomSheetPersonalListBinding.edtListName.setText(data.listName)
                        bottomSheetPersonalListBinding.edtDeskripsi.setText(data.listDescription)
                        Snackbar.make(binding.root, "${data.listName}, ${list.statusMessage}", Snackbar.LENGTH_SHORT).show()
                        bottomSheet.dismiss()
                    }
                }
            }
        }
    }

    //detail data user
    private fun setDataUser() {
        val sessionId = PrefsAuthentikasi(this).getSessionId()
        if (sessionId != null) {
            loginViewModel.getAccountDetail(sessionId, sessionId).observe(this) { detail ->
                if (detail != null) {

                    binding.tvUsername.text = buildString {
                        append(detail.username)
                        append(" \u2022 ")
                        append(detail.iso6391)
                    }

                    Glide.with(this)
                        .load(URL_IMAGE_PROFILE)
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.imgProfile)
                }
            }
        }
    }
}