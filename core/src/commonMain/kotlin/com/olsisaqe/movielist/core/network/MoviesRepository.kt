package com.olsisaqe.movielist.core.network

import com.olsisaqe.movielist.core.model.MoviesData
import com.olsisaqe.movielist.core.model.MoviesDetail

interface MoviesRepository {
    suspend fun moviesData(): MoviesData
    suspend fun moviesDetail(): MoviesDetail
}