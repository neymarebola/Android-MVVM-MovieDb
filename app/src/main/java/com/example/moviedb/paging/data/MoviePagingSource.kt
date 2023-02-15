package com.example.moviedb.paging.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviedb.models.Result
import com.example.moviedb.repository.MoviesRepository
import com.example.moviedb.util.Constants
import kotlinx.coroutines.delay
import kotlin.math.max

private const val STARTING_KEY = 1
private const val LOAD_DELAY_MILLIS = 3_000L

class MoviePagingSource(val repository: MoviesRepository, val categoryId: Int) :
    PagingSource<Int, Result>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        // If params.key is null, it is the first load, so we start loading with STARTING_KEY
        val startKey = params.key ?: STARTING_KEY

        // We fetch as many articles as hinted to by params.loadSize
        val range = startKey.until(startKey + params.loadSize)

        // Simulate a delay for loads adter the initial load
        if (startKey != STARTING_KEY) delay(LOAD_DELAY_MILLIS)
        if (categoryId == Constants.POPULAR) {
            val response = repository.getPopularMovies(startKey)
            val responseData = mutableListOf<Result>()
            val data = response.body()?.results ?: emptyList()
            responseData.addAll(data)

            return LoadResult.Page(
                data = responseData,
                prevKey = when (startKey) {
                    STARTING_KEY -> null
                    else -> when (val prevKey =
                        ensureValidKey(key = range.first - params.loadSize)) {
                        // We're at the start, there's nothing more to load
                        STARTING_KEY -> null
                        else -> prevKey
                    }
                },
                nextKey = range.last + 1
            )
        }

        if (categoryId == Constants.TOP_RATED) {
            val response = repository.getTopRatedMovies(startKey)
            val responseData = mutableListOf<Result>()
            val data = response.body()?.results ?: emptyList()
            responseData.addAll(data)

            return LoadResult.Page(
                data = responseData,
                prevKey = when (startKey) {
                    STARTING_KEY -> null
                    else -> when (val prevKey =
                        ensureValidKey(key = range.first - params.loadSize)) {
                        // We're at the start, there's nothing more to load
                        STARTING_KEY -> null
                        else -> prevKey
                    }
                },
                nextKey = range.last + 1
            )
        } else {
            val response = repository.getUpComingMovies(startKey)
            val responseData = mutableListOf<Result>()
            val data = response.body()?.results ?: emptyList()
            responseData.addAll(data)

            return LoadResult.Page(
                data = responseData,
                prevKey = when (startKey) {
                    STARTING_KEY -> null
                    else -> when (val prevKey =
                        ensureValidKey(key = range.first - params.loadSize)) {
                        // We're at the start, there's nothing more to load
                        STARTING_KEY -> null
                        else -> prevKey
                    }
                },
                nextKey = range.last + 1
            )
        }

    }

    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)
}