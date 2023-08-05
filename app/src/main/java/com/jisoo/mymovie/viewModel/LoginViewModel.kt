package com.jisoo.mymovie.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jisoo.mymovie.data.repository.Repositories
import com.jisoo.mymovie.response.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val repositories: Repositories): ViewModel(){

    val guest = repositories.getGuestSession().asLiveData()

    val requestToken = repositories.getRequestToken().asLiveData()

    fun validateWithLogin(token: String, data: LoginRequest) = repositories.validateWithLogin(token, data).asLiveData()

    fun createSessionId(token: String) = repositories.createSessionId(token).asLiveData()

    fun getAccountDetail(accountId: String, sessionId: String) = repositories.getAccountDetail(accountId, sessionId).asLiveData()
}