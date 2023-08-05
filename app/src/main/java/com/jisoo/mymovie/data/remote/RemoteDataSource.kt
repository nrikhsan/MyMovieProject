package com.jisoo.mymovie.data.remote

import com.jisoo.mymovie.data.network.service.ApiService
import com.jisoo.mymovie.response.LoginRequest
import com.jisoo.mymovie.response.MovieId
import com.jisoo.mymovie.response.RatingRequest
import com.jisoo.mymovie.response.RequestCreateList
import com.jisoo.mymovie.utils.Constants.API_KEY
import com.jisoo.mymovie.utils.EnumClassSortBy
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getGuestSession() = apiService.getGuestSession(API_KEY)

    suspend fun getRequestToken() = apiService.getRequestToken(API_KEY)

    suspend fun validateWithLogin(token: String, data: LoginRequest) = apiService.validateWithLogin(API_KEY, token, data)

    suspend fun createSessionId(token: String) = apiService.createSessionId(API_KEY, token)

    suspend fun getAccountDetail(accountId: String, sessionId: String) = apiService.getAccountDetail(accountId, API_KEY, sessionId)

    suspend fun getDiscoverMovie() = apiService.getDiscover(API_KEY)

    suspend fun getDetailMovie(movieId: String) = apiService.getDetailMovie(movieId, API_KEY)

    suspend fun getRatedMoviesBySessionId(accountId: String, sessionId: String) = apiService.getRatedMoviesBySessionId(accountId, API_KEY, sessionId)

    suspend fun getRatedMoviesByGuest(guestSessionId: String) = apiService.getRatedMoviesByGuest(guestSessionId, API_KEY)

    suspend fun createList(sessionId: String, data: RequestCreateList) = apiService.createList(API_KEY, sessionId, data)

    suspend fun getPersonalList(accountId: String, sessionId: String) = apiService.getPersonalList(accountId, API_KEY, sessionId)

    suspend fun getDetailPersonalList(listId: String) = apiService.getDetailPersonalList(listId, API_KEY)

    suspend fun searchMovieAndAddToPersonalList(query: String) = apiService.searchMovieAndAddToPersonalList(query, API_KEY)

    suspend fun addRating(movieId: String, sessionId: String, data: RatingRequest) = apiService.addRatingMovie(movieId, API_KEY, sessionId, data)

    suspend fun deleteRating(movieId: String, sessionId: String) = apiService.deleteRating(movieId, API_KEY, sessionId)

    suspend fun deleteList(listId: String, sessionId: String) = apiService.deleteList(listId, API_KEY, sessionId)

    suspend fun addMovieToPersonalList(listId: String, sessionId: String, mediaId: MovieId) = apiService.addMovieToPersonalList(listId, API_KEY, sessionId, mediaId)
}