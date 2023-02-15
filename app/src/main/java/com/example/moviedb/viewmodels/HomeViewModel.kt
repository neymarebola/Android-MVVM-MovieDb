package com.example.moviedb.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.models.MoviesResponse
import com.example.moviedb.models.Result
import com.example.moviedb.repository.MoviesRepository
import kotlinx.coroutines.launch

class HomeViewModel(val movieRepository: MoviesRepository) : ViewModel() {
    val topTenPopularMovies = MutableLiveData<List<Result>>()
    val topTenTopRatedMovies = MutableLiveData<List<Result>> ()
    val topTenNowPlayingMovies = MutableLiveData<List<Result>> ()
    val topTenUpComingMovies = MutableLiveData<List<Result>> ()

    init {
        getTopTenPopularMovies(1)
        getTopTenTopRatedMovies(1)
        getTopTenNowPlayingMovies(1)
        getTopTenUpComingMovies(1)
    }

    fun getTopTenPopularMovies(page: Int): List<Result>? {
        viewModelScope.launch {
            val response = movieRepository.getPopularMovies(page)
            if (response.isSuccessful) {
                response.body()?.let { it ->
                    val tmp = mutableListOf<Result>()
                    for (i in 1..10) {
                        tmp.add(it.results[i])
                    }
                    topTenPopularMovies.postValue(tmp)
                }
            }
        }
        return topTenPopularMovies.value
    }

    fun getTopTenTopRatedMovies(page: Int): List<Result>? {
        viewModelScope.launch {
            val response = movieRepository.getTopRatedMovies(page)
            if (response.isSuccessful) {
                response.body()?.let { it ->
                    val tmp = mutableListOf<Result>()
                    for (i in 1..10) {
                        tmp.add(it.results[i])
                    }
                    topTenTopRatedMovies.postValue(tmp)
                }
            }
        }
        return topTenTopRatedMovies.value
    }

    fun getTopTenNowPlayingMovies(page: Int): List<Result>? {
        viewModelScope.launch {
            val response = movieRepository.getNowPlayingMovies(page)
            if (response.isSuccessful) {
                response.body()?.let { it ->
                    val tmp = mutableListOf<Result>()
                    for (i in 1..10) {
                        tmp.add(it.results[i])
                    }
                    topTenNowPlayingMovies.postValue(tmp)
                }
            }
        }
        return topTenNowPlayingMovies.value
    }

    fun getTopTenUpComingMovies(page: Int): List<Result>? {
        viewModelScope.launch {
            val response = movieRepository.getUpComingMovies(page)
            if (response.isSuccessful) {
                response.body()?.let { it ->
                    val tmp = mutableListOf<Result>()
                    for (i in 1..10) {
                        tmp.add(it.results[i])
                    }
                    topTenUpComingMovies.postValue(tmp)
                }
            }
        }
        return topTenUpComingMovies.value
    }


}