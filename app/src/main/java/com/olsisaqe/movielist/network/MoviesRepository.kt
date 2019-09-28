package com.olsisaqe.movielist.network

import com.olsisaqe.movielist.model.api.MoviesData
import com.olsisaqe.movielist.model.api.MoviesDetail

interface MoviesRepository {
    @Throws(Throwable::class)
    suspend fun moviesData(): MoviesData
    @Throws(Throwable::class)
    suspend fun moviesDetail(): MoviesDetail
}