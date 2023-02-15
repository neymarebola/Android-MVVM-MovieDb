package com.example.moviedb.api

import com.example.moviedb.models.MovieDetailResponse
import com.example.moviedb.models.MoviesResponse
import com.example.moviedb.models.ReviewsResponse
import com.example.moviedb.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @GET("popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en",
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): Response<MoviesResponse>

    @GET("top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = "en"
    ): Response<MoviesResponse>

    @GET("now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = "en"
    ): Response<MoviesResponse>

    @GET("upcoming")
    suspend fun getUpComingMovies(
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = "en"
    ): Response<MoviesResponse>

    @GET("movie")
    suspend fun searchMovies(
        @Query("query") query: String = "",
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = "en"
    ): Response<MoviesResponse>

    @GET("{id}")
    suspend fun getMovieDetail(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = "en",
        @Query("append_to_response") video: String = "videos"
    ): Response<MovieDetailResponse>

    @GET("{id}/similar")
    suspend fun getSimilarMovies(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = "en",
        @Query("page") page: Int = 1
    ): Response<MoviesResponse>

    @GET("{id}/reviews")
    suspend fun getReviews(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = "en",
        @Query("page") page: Int = 1
    ): Response<ReviewsResponse>

}