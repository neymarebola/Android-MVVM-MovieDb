package com.example.moviedb.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviedb.models.Result
import com.example.moviedb.paging.data.MoviePagingSource
import com.example.moviedb.repository.MoviesRepository
import com.example.moviedb.util.Constants
import kotlinx.coroutines.flow.Flow


private const val ITEMS_PER_PAGE = 20

class MoviesByCategoryViewModel(val movieRepository: MoviesRepository) : ViewModel() {
    //    val allPopularMovies: Flow<PagingData<Result>> = Pager(
//        config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = false),
//        pagingSourceFactory = { MoviePagingSource(movieRepository, Constants.POPULAR) }
//    )
//        .flow
//        .cachedIn(viewModelScope)
    val allPopularMovies = Pager(PagingConfig(pageSize = 5)) {
        MoviePagingSource(repository = movieRepository, Constants.POPULAR)
    }.flow.cachedIn(viewModelScope)

    val allTopRatedMovies = Pager(PagingConfig(pageSize = 5)) {
        MoviePagingSource(repository = movieRepository, Constants.TOP_RATED)
    }.flow.cachedIn(viewModelScope)

    val allUpComingMovies = Pager(PagingConfig(pageSize = 5)) {
        MoviePagingSource(repository = movieRepository, Constants.UP_COMING)
    }.flow.cachedIn(viewModelScope)
}