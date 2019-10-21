package com.olsisaqe.movielist.core.model

import kotlinx.serialization.Serializable

@Serializable
data class MoviesDetail(val image_base: String, val movie_offers: List<MovieDetail>)