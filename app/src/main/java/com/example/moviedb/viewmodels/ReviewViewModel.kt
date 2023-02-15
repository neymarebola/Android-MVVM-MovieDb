package com.example.moviedb.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.models.Review
import com.example.moviedb.repository.MoviesRepository
import kotlinx.coroutines.launch

class ReviewViewModel(val repository: MoviesRepository) : ViewModel(){

    val reviews = MutableLiveData<List<Review>> ()

    fun getListReviews(movieId: Int) = viewModelScope.launch {
        val response = repository.getReviews(movieId)
        if (response.isSuccessful) {
            response.body()?.let {
                reviews.value = it.results
            }
        }
    }

}