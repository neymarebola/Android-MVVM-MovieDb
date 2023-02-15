package com.example.moviedb.repository

import com.example.moviedb.api.RetrofitInstance

class MoviesRepository {
    suspend fun getPopularMovies(page: Int) = RetrofitInstance.api.getPopularMovies(page)

    suspend fun getTopRatedMovies(page: Int) = RetrofitInstance.api.getTopRatedMovies(page)

    suspend fun getNowPlayingMovies(page: Int) = RetrofitInstance.api.getNowPlayingMovies(page)

    suspend fun getUpComingMovies(page: Int) = RetrofitInstance.api.getUpComingMovies(page)

    suspend fun searchMovies(query: String) = RetrofitInstance.searchApi.searchMovies(query)

    suspend fun getMovieDetail(id: Int) = RetrofitInstance.api.getMovieDetail(id)

    suspend fun getSimilarMovies(id: Int) = RetrofitInstance.api.getSimilarMovies(id)

    suspend fun getReviews(id: Int) = RetrofitInstance.api.getReviews(id)

}