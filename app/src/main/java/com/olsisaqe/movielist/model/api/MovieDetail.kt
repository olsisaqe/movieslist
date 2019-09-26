package com.olsisaqe.movielist.model.api

import kotlinx.serialization.Serializable

@Serializable
data class MovieDetail(
    val movie_id: Int,
    val price: String,
    val image: String,
    val available: Boolean
)