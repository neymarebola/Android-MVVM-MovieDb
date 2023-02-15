package com.example.moviedb.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.models.Result
import com.example.moviedb.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(private val repository: MoviesRepository) : ViewModel() {

    val searchMovies = MutableLiveData<List<Result>>()

    fun getSearchMovies(query: String) = viewModelScope.launch {
        val response = repository.searchMovies(query)
        if (response.isSuccessful) {
            response.body()?.let { moviesResponse ->
                searchMovies.postValue(moviesResponse.results)
            }
        }
    }
}