package com.olsisaqe.movielist.core.network

import com.olsisaqe.movielist.core.model.MoviesData
import com.olsisaqe.movielist.core.model.MoviesDetail
import com.olsisaqe.movielist.core.model.ui.Movie

class MoviesService(private val moviesRepository: MoviesRepository) {

    suspend fun getMovies(): List<Movie> {
        val moviesData = moviesRepository.moviesData()
        val moviesDetail = moviesRepository.moviesDetail()
        return mapToMovie(moviesData, moviesDetail)
    }

    private fun mapToMovie(moviesData: MoviesData, moviesDetail: MoviesDetail): List<Movie> {
        val moviesOfferIds = moviesDetail.movie_offers.map { it.movie_id }
        val moviesDetailIds = moviesData.movie_data.map { it.movie_id }
        val mergedIdList = moviesOfferIds.union(moviesDetailIds)
        return mergedIdList.map { movieId ->
            val movieData = moviesData.movie_data.firstOrNull { it.movie_id == movieId }
            val movieDetail = moviesDetail.movie_offers.firstOrNull { it.movie_id == movieId }
            val imageUrl = movieDetail?.let { moviesDetail.image_base + it.image }
            Movie(
                movieId,
                movieDetail?.price,
                imageUrl,
                movieDetail?.available,
                movieData?.title,
                movieData?.sub_title
            )
        }
    }
}