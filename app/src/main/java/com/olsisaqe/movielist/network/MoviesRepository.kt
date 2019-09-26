package com.olsisaqe.movielist.network

import com.olsisaqe.movielist.model.api.MoviesData
import com.olsisaqe.movielist.model.api.MoviesDetail

interface MoviesRepository {
    suspend fun moviesData(): MoviesData
    suspend fun moviesDetail(): MoviesDetail
}