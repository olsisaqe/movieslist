package com.olsisaqe.movielist

import android.app.Application
import com.olsisaqe.movielist.model.Environment
import com.olsisaqe.movielist.model.api.MovieData
import com.olsisaqe.movielist.model.api.MovieDetail
import com.olsisaqe.movielist.model.api.MoviesData
import com.olsisaqe.movielist.model.api.MoviesDetail
import com.olsisaqe.movielist.network.MoviesRepository
import com.olsisaqe.movielist.network.MoviesRest
import io.ktor.client.HttpClient
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
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

@KtorExperimentalAPI
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }

    @UseExperimental(UnstableDefault::class)
    private val appModule = module {
        single {
            Environment("dev", "https://7f6589e2-26c2-4250-8c0d-f2ff7eb67872.mock.pstmn.io")
        }
        single {
            HttpClient {
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
        }
        factory<MoviesRepository> {
            MoviesRest(get(), get())
        }
        viewModel {
            MainViewModel(get())
        }
    }
}