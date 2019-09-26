package com.olsisaqe.movielist.model.api

import kotlinx.serialization.Serializable

@Serializable
data class MoviesData(val movie_data: List<MovieData>)