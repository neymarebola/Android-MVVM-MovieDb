package com.example.moviedb.models

import java.util.*

data class MovieDetailResponse(
    val budget: Int = 0,
    val homepage: String = "",
    val id: Int = 0,
    val original_title: String = "",
    val original_language: String = "",
    val overview: String = "",
    val poster_path: String = "",
    val release_date: String = "",
    val revenue: Int = 0,
    val status: String = "",
    val title: String = "",
    val vote_average: Double = 0.0,
    val videos: Videos
)
