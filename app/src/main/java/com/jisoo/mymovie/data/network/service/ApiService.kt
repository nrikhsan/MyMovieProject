package com.jisoo.mymovie.data.network.service

import com.jisoo.mymovie.response.LoginRequest
import com.jisoo.mymovie.response.MovieId
import com.jisoo.mymovie.response.PersonalListMovies
import com.jisoo.mymovie.response.RatingRequest
import com.jisoo.mymovie.response.RequestCreateList
import com.jisoo.mymovie.response.ResponseAccount
import com.jisoo.mymovie.response.ResponseAddMovieToPersonalList
import com.jisoo.mymovie.response.ResponseAddRating
import com.jisoo.mymovie.response.ResponseCreatedList
import com.jisoo.mymovie.response.ResponseDeletedList
import com.jisoo.mymovie.response.ResponseDetail
import com.jisoo.mymovie.response.ResponseDetailPersonalList
import com.jisoo.mymovie.response.ResponseGuest
import com.jisoo.mymovie.response.ResponseMovies
import com.jisoo.mymovie.response.ResponsePersonalList
import com.jisoo.mymovie.response.ResponseRequestToken
import com.jisoo.mymovie.response.ResponseSessionId
import com.jisoo.mymovie.utils.Constants.ACCOUNT_DETAIL
import com.jisoo.mymovie.utils.Constants.ADD_MOVIE_TO_PERSONAL_LIST
import com.jisoo.mymovie.utils.Constants.ADD_RATING
import com.jisoo.mymovie.utils.Constants.CREATE_LIST
import com.jisoo.mymovie.utils.Constants.CREATE_SESSION
import com.jisoo.mymovie.utils.Constants.DELETE_LIST
import com.jisoo.mymovie.utils.Constants.DELETE_RATING
import com.jisoo.mymovie.utils.Constants.DETAIL_MOVIE
import com.jisoo.mymovie.utils.Constants.DETAIL_PERSONAL_LIST
import com.jisoo.mymovie.utils.Constants.DISCOVER
import com.jisoo.mymovie.utils.Constants.GUEST_SESSION
import com.jisoo.mymovie.utils.Constants.PERSONAL_LIST
import com.jisoo.mymovie.utils.Constants.RATED_MOVIES_BY_GUEST
import com.jisoo.mymovie.utils.Constants.RATED_MOVIES_BY_SESSION_ID
import com.jisoo.mymovie.utils.Constants.REQUEST_TOKEN
import com.jisoo.mymovie.utils.Constants.SEARCH_MOVIE_FOR_ADD_TO_PERSONAL_LIST
import com.jisoo.mymovie.utils.Constants.VALIDATE_WITH_LOGIN
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(GUEST_SESSION)
    suspend fun getGuestSession(@Query("api_key") apiKey: String): Response<ResponseGuest>

    @GET(REQUEST_TOKEN)
    suspend fun getRequestToken(@Query("api_key") apiKey: String): Response<ResponseRequestToken>

    @POST(VALIDATE_WITH_LOGIN)
    suspend fun validateWithLogin(
        @Query("api_key") apiKey: String,
        @Query("request_token") token: String,
        @Body loginRequest: LoginRequest
    ): Response<ResponseRequestToken>

    @POST(CREATE_SESSION)
    suspend fun createSessionId(
        @Query("api_key") apiKey: String,
        @Query("request_token") token: String
    ): Response<ResponseSessionId>

    @GET(ACCOUNT_DETAIL)
    suspend fun getAccountDetail(
        @Path("account_id") accountId: String,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Response<ResponseAccount>

    @GET(DISCOVER)
    suspend fun getDiscover(
        @Query("api_key") apiKey: String
    ): Response<ResponseMovies>

    @GET(DETAIL_MOVIE)
    suspend fun getDetailMovie(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String
    ): Response<ResponseDetail>

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST(ADD_RATING)
    suspend fun addRatingMovie(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String,
        @Body ratingRequest: RatingRequest
    ): Response<ResponseAddRating>

    @Headers("Content-Type: application/json;charset=utf-8")
    @DELETE(DELETE_RATING)
    suspend fun deleteRating(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String,
    ): Response<ResponseAddRating>

    @GET(RATED_MOVIES_BY_SESSION_ID)
    suspend fun getRatedMoviesBySessionId(
        @Path("account_id") accountId: String,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Response<ResponseMovies>

    @GET(RATED_MOVIES_BY_GUEST)
    suspend fun getRatedMoviesByGuest(
        @Path("guest_session_id") guestSessionId: String,
        @Query("api_key") apiKey: String
    ): Response<ResponseMovies>

    @POST(CREATE_LIST)
    suspend fun createList(
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String,
        @Body requestCreateList: RequestCreateList
    ): Response<ResponseCreatedList>

    @GET(PERSONAL_LIST)
    suspend fun getPersonalList(
        @Path("account_id") accountId: String,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String,
    ): Response<ResponsePersonalList>
    
    @GET(DETAIL_PERSONAL_LIST)
    suspend fun getDetailPersonalList(
        @Path("list_id") listId: String,
        @Query("api_key") apiKey: String
    ): Response<ResponseDetailPersonalList>

    @GET(SEARCH_MOVIE_FOR_ADD_TO_PERSONAL_LIST)
    suspend fun searchMovieAndAddToPersonalList(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    ): Response<ResponseMovies>

    @POST(ADD_MOVIE_TO_PERSONAL_LIST)
    suspend fun addMovieToPersonalList(
        @Path("list_id") listId: String,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String,
        @Body movieId: MovieId
    ): Response<ResponseAddMovieToPersonalList>

    @DELETE(DELETE_LIST)
    suspend fun deleteList(
        @Path("list_id") listId: String,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String,
    ): Response<ResponseDeletedList>

}