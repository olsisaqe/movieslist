package com.olsisaqe.movielist.core.network

import com.olsisaqe.movielist.core.model.Environment
import com.olsisaqe.movielist.core.model.MoviesData
import com.olsisaqe.movielist.core.model.MovieData
import com.olsisaqe.movielist.core.model.MoviesDetail
import com.olsisaqe.movielist.core.model.MovieDetail
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

class MoviesRest() : MoviesRepository {

    val client = HttpClient {
        install(JsonFeature) {
            acceptContentTypes = listOf(
                ContentType.Application.Json,
                ContentType.parse("text/html") // the api content is html instead of json
            )
            serializer = KotlinxSerializer(Json.nonstrict).apply {
                setMapper(MoviesData::class, MoviesData.serializer())
                setMapper(MovieData::class, MovieData.serializer())
                setMapper(MovieDetail::class, MovieDetail.serializer())
                setMapper(MoviesDetail::class, MoviesDetail.serializer())
            }
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
    }

    val environment =
        Environment("dev", "https://7f6589e2-26c2-4250-8c0d-f2ff7eb67872.mock.pstmn.io")

    override suspend fun moviesData() =
        client.get<MoviesData>("${environment.httpBaseUrl}/movie-data")

    override suspend fun moviesDetail() =
        client.get<MoviesDetail>("${environment.httpBaseUrl}/movie-offers")
}