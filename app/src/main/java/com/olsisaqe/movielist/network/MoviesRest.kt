package com.olsisaqe.movielist.network

import com.olsisaqe.movielist.model.Environment
import com.olsisaqe.movielist.model.api.MoviesData
import com.olsisaqe.movielist.model.api.MoviesDetail
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class MoviesRest(
    private val client: HttpClient,
    private val environment: Environment
) : MoviesRepository {
    override suspend fun moviesData() =
        client.get<MoviesData>("${environment.httpBaseUrl}/movie-data")

    override suspend fun moviesDetail() =
        client.get<MoviesDetail>("${environment.httpBaseUrl}/movie-offers")
}