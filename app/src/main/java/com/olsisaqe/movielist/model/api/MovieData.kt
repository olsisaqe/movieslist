package com.olsisaqe.movielist.model.api

import kotlinx.serialization.Serializable

@Serializable
data class MovieData(val movie_id: Int, val title: String, val sub_title: String)