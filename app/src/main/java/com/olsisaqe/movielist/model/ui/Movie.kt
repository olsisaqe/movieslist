package com.olsisaqe.movielist.model.ui

data class Movie(
    val movieId: Int,
    val price: String?,
    val image: String?,
    val available: Boolean?,
    val title: String?,
    val subtitle: String?
)