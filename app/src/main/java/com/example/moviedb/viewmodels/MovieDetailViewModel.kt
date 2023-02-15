package com.example.moviedb.viewmodels

import androidx.lifecycle.*
import com.example.moviedb.models.MovieDetailResponse
import com.example.moviedb.models.MoviesResponse
import com.example.moviedb.models.Video
import com.example.moviedb.repository.MoviesRepository
import com.example.moviedb.util.Constants
import kotlinx.coroutines.launch

class MovieDetailViewModel(val repository: MoviesRepository) : ViewModel(){
     val movieDetail = MutableLiveData<MovieDetailResponse> ()
     val similarMovies = MutableLiveData<MoviesResponse> ()

     val navigateToYoutube = MutableLiveData<String> ()

     fun getMovieDetail(id: Int) = viewModelScope.launch {
          val response = repository.getMovieDetail(id)
          if (response.isSuccessful) {
               response.body().let {
                    movieDetail.postValue(it)
               }
          }
     }

     fun getSimilarMovies(id: Int) = viewModelScope.launch {
          val response = repository.getSimilarMovies(id)
          if (response.isSuccessful) {
               response.body().let {
                    similarMovies.value = it
               }
          }
     }

     fun openYoutube(video: Video) {
          navigateToYoutube.value = Constants.YOUTUBE_BASE_URL + video.key
     }

}