package com.olsisaqe.movielist.core.ios

import com.olsisaqe.movielist.core.model.ui.Movie
import com.olsisaqe.movielist.core.network.MoviesRepository
import com.olsisaqe.movielist.core.network.MoviesService
import kotlinx.coroutines.launch

class MoviesServiceCallback(private val moviesRepository: MoviesRepository) {

    private val moviesService by lazy { MoviesService(moviesRepository) }

    fun getMovies(completion: (List<Movie>) -> Unit) {
        MainScope().launch {
            completion(moviesService.getMovies())
        }
    }
}