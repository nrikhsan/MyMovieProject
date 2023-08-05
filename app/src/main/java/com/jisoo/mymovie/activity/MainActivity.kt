package com.jisoo.mymovie.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.jisoo.mymovie.databinding.ActivityMainBinding
import com.jisoo.mymovie.response.LoginRequest
import com.jisoo.mymovie.utils.Constants.URL_TMDB_MOVIE
import com.jisoo.mymovie.utils.PrefsAuthentikasi
import com.jisoo.mymovie.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTamu.setOnClickListener {
            guestSession(loginViewModel)
        }

        binding.btnMasuk.setOnClickListener {
            setRequestToken()
        }

        binding.tvTmdb.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(URL_TMDB_MOVIE)
            startActivity(intent)
        }
    }

    private fun setRequestToken() {
        val username = binding.edtUsername.text.toString()
        val password = binding.edtPassword.text.toString()

        if (username.isEmpty() && password.isEmpty()) {
            Snackbar.make(binding.root, "Form input tidak boleh ada yang kosong", Snackbar.LENGTH_SHORT).show()
        }else if (username.isEmpty()){
            Snackbar.make(binding.root, "Username tidak boleh kosong", Snackbar.LENGTH_SHORT).show()
        }else if (password.isEmpty()){
            Snackbar.make(binding.root, "Password tidak boleh kosong", Snackbar.LENGTH_SHORT).show()
        }else{
            requestToken(username, password)
        }
    }

    //meminta token
    private fun requestToken(username: String, password: String) {
        loginViewModel.requestToken.observe(this) {
            if (it != null) {
                val data = LoginRequest(username, password)
                validateWithLogin(it, data)
            }
        }
    }

    //memvalidasi token yang sudah diambil
    private fun validateWithLogin(requestToken: String, data: LoginRequest) {
        loginViewModel.validateWithLogin(requestToken, data).observe(this){
            if (it != null){
                binding.edtUsername.setText(data.username)
                binding.edtPassword.setText(data.password)

                val intent = Intent(this, BerandaActivity::class.java)
                startActivity(intent)
                finish()

                createSessionId(it)
            }
        }
    }

    //mengambil session id setelah token tervalidasi
    private fun createSessionId(requestToken: String) {
        loginViewModel.createSessionId(requestToken).observe(this){}
    }


    //masuk sebagai tamu, tanpa melakukan autentikasi
    private fun guestSession(loginViewModel: LoginViewModel) {
        loginViewModel.guest.observe(this){ guest->
            if (guest != null){
                val intent = Intent(this, BerandaActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    //jika sudah login maka tidak perlu melakukan autentikasi lagi
    override fun onStart() {
        super.onStart()
        val prefsAuthentikasi = PrefsAuthentikasi(this)
        if (prefsAuthentikasi.isLogin() == true){
            val intent = Intent(this, BerandaActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}