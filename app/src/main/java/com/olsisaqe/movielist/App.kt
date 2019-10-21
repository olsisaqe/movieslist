package com.olsisaqe.movielist

import android.app.Application
import com.olsisaqe.movielist.core.network.MoviesRepository
import com.olsisaqe.movielist.core.network.MoviesRest
import com.olsisaqe.movielist.core.network.MoviesService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

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

    private val appModule = module {
        factory<MoviesRepository> {
            MoviesRest()
        }
        factory { MoviesService(get()) }
        viewModel {
            MainViewModel(get())
        }
    }
}