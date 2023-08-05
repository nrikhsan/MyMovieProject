package com.jisoo.mymovie.data.repository

import android.content.Context
import android.util.Log
import com.jisoo.mymovie.data.remote.RemoteDataSource
import com.jisoo.mymovie.response.LoginRequest
import com.jisoo.mymovie.response.MovieId
import com.jisoo.mymovie.response.RatingRequest
import com.jisoo.mymovie.response.RequestCreateList
import com.jisoo.mymovie.utils.PrefsAuthentikasi
import com.jisoo.mymovie.utils.PrefsRating
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Repositories @Inject constructor(private val context: Context, private val remoteDataSource: RemoteDataSource) {

    private val pref = PrefsAuthentikasi(context)
    private val prefsRating = PrefsRating(context)
    companion object{
        const val TAG = "TAG"
    }

    fun getGuestSession() = flow {
        try {
            remoteDataSource.getGuestSession().let {
                if (it.isSuccessful){
                    val guest = it.body()?.guestSessionId
                    emit(guest)
                    pref.setGuestSession(guest)
                    Log.e(TAG, guest.toString())
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }

    fun getRequestToken() = flow {
        try {
            remoteDataSource.getRequestToken().let {
                if (it.isSuccessful){
                    pref.setLogin(true)
                    val token = it.body()?.requestToken
                    emit(token)
                    Log.e(TAG, token.toString())
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }

    fun validateWithLogin(token: String, data: LoginRequest) = flow {
        try {
            remoteDataSource.validateWithLogin(token, data).let {
                if (it.isSuccessful){
                    pref.setLogin(true)
                    val validateWithLogin = it.body()?.requestToken
                    emit(validateWithLogin)
                    Log.e(TAG, validateWithLogin.toString())
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }

    fun createSessionId(token: String) = flow {
        try {
            remoteDataSource.createSessionId(token).let {
                if (it.isSuccessful){
                    pref.setLogin(true)
                    val sessionId = it.body()?.sessionId
                    emit(sessionId)
                    pref.setSession(sessionId)
                    Log.e(TAG, sessionId.toString())
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }

    fun getAccountDetail(accountId: String, sessionId: String) = flow {
        try {
            remoteDataSource.getAccountDetail(accountId, sessionId).let {
                if (it.isSuccessful){
                    pref.setLogin(true)
                    val account = it.body()
                    emit(account)
                    Log.e(TAG, account.toString())
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }

    fun getDiscoverMovie() = flow {
        try {
            remoteDataSource.getDiscoverMovie().let {
                if (it.isSuccessful){
                    val discover = it.body()?.results
                    emit(discover)
                    Log.e(TAG, discover.toString())
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }

    fun getDetailMovie(movieId: String) = flow {
        try {
            remoteDataSource.getDetailMovie(movieId).let {
                if (it.isSuccessful){
                    val detail = it.body()
                    emit(detail)
                    Log.e(TAG, detail.toString())
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }

    fun getRatedMoviesBySessionId(accountId: String, sessionId: String) = flow {
        try {
            remoteDataSource.getRatedMoviesBySessionId(accountId, sessionId).let {
                if (it.isSuccessful){
                    val rated = it.body()?.results
                    emit(rated)
                    Log.e(TAG, rated.toString())
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }

    fun getRatedMoviesByGuest(guest: String) = flow {
        try {
            remoteDataSource.getRatedMoviesByGuest(guest).let {
                if (it.isSuccessful){
                    val rated = it.body()?.results
                    emit(rated)
                    Log.e(TAG, rated.toString())
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }

    fun createList(sessionId: String, data: RequestCreateList) = flow {
        try {
            remoteDataSource.createList(sessionId, data).let {
                if (it.isSuccessful){
                    val list = it.body()
                    emit(list)
                    Log.e(TAG, list.toString())
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }

    fun getDetailPersonalList(listId: String) = flow {
        try {
            remoteDataSource.getDetailPersonalList(listId).let {
                if (it.isSuccessful){
                    val list = it.body()
                    emit(list)
                    Log.e(TAG, list.toString())
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }

    fun searchMovieAndAddToPersonalList(query: String) = flow {
        try {
            remoteDataSource.searchMovieAndAddToPersonalList(query).let {
                if (it.isSuccessful){
                    val list = it.body()
                    emit(list)
                    Log.e(TAG, list.toString())
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }

    fun addMovieToPersonalList(listId: String, sessionId: String, movieId: MovieId) = flow {
        try {
            remoteDataSource.addMovieToPersonalList(listId, sessionId, movieId).let {
                if (it.isSuccessful){
                    val list = it.body()
                    emit(list)
                    Log.e(TAG, list.toString())
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }

    fun deletedList(listId: String, sessionId: String) = flow {
        try {
            remoteDataSource.deleteList(listId, sessionId).let {
                if (it.isSuccessful){
                    val list = it.body()
                    emit(list)
                    Log.e(TAG, list.toString())
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }

    fun getPersonalList(accountId: String, sessionId: String) = flow {
        try {
            remoteDataSource.getPersonalList(accountId, sessionId).let {
                if (it.isSuccessful){
                    val list = it.body()?.results
                    emit(list)
                    Log.e(TAG, list.toString())
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }

    fun addRating(movieId: String, sessionId: String, data: RatingRequest) = flow {
        try {
            remoteDataSource.addRating(movieId, sessionId, data).let {
                if (it.isSuccessful){
                    pref.setLogin(true)
                    val rating = it.body()
                    emit(rating)
                    prefsRating.setRating(movieId, data.value)
                    Log.e(TAG, rating.toString())
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }

    fun deleteRating(movieId: String, sessionId: String) = flow {
        try {
            remoteDataSource.deleteRating(movieId, sessionId).let {
                if (it.isSuccessful){
                    val rating = it.body()
                    emit(rating)
                    Log.e(TAG, rating.toString())
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
        }
    }
}