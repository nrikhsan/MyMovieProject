package com.jisoo.mymovie.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jisoo.mymovie.data.repository.Repositories
import com.jisoo.mymovie.response.MovieId
import com.jisoo.mymovie.response.RatingRequest
import com.jisoo.mymovie.response.RequestCreateList
import com.jisoo.mymovie.utils.EnumClassSortBy
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MovieViewModel @Inject constructor(private val repositories: Repositories): ViewModel(){

    val discover = repositories.getDiscoverMovie().asLiveData()

    fun getDetailMovie(movieId: String) = repositories.getDetailMovie(movieId).asLiveData()

    fun getRatedMovieBySessionId(accountId: String, sessionId: String) = repositories.getRatedMoviesBySessionId(accountId, sessionId).asLiveData()

    fun getRatedMovieByGuest(guest: String) = repositories.getRatedMoviesByGuest(guest).asLiveData()

    fun createList(sessionId: String, data: RequestCreateList) = repositories.createList(sessionId, data).asLiveData()

    fun getPersonalList(accountId: String, sessionId: String) = repositories.getPersonalList(accountId, sessionId).asLiveData()

    fun addRatingMovie(movieId: String, sessionId: String, data: RatingRequest) = repositories.addRating(movieId, sessionId, data).asLiveData()

    fun deleteRating(movieId: String, sessionId: String) = repositories.deleteRating(movieId, sessionId).asLiveData()

    fun deletedList(listId: String, sessionId: String) = repositories.deletedList(listId, sessionId).asLiveData()

    fun getDetailPersonalList(listId: String) = repositories.getDetailPersonalList(listId).asLiveData()

    fun searchMovieAndAddToPersonalList(query: String) = repositories.searchMovieAndAddToPersonalList(query).asLiveData()

    fun addMovieToPersonalList(listId: String, sessionId: String, movieId: MovieId) = repositories.addMovieToPersonalList(listId, sessionId, movieId)
        .asLiveData()
}