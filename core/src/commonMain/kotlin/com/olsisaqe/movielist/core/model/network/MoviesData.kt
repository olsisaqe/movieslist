package com.olsisaqe.movielist.core.model

import kotlinx.serialization.Serializable

@Serializable
data class MoviesData(val movie_data: List<MovieData>)