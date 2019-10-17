package com.olsisaqe.movielist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olsisaqe.movielist.core.model.MoviesData
import com.olsisaqe.movielist.core.model.MoviesDetail
import com.olsisaqe.movielist.core.network.MoviesRepository
import com.olsisaqe.movielist.model.ui.Movie
import com.olsisaqe.movielist.model.ui.Resource
import kotlinx.coroutines.launch

class MainViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {
    private val _moviesLiveData = MutableLiveData<Resource<List<Movie>>>()
    val moviesLiveData: LiveData<Resource<List<Movie>>> = _moviesLiveData

    init {
        fetchData()
    }

    fun refresh() {
        if (_moviesLiveData.value?.status != Resource.Status.LOADING) fetchData()
    }

    private fun fetchData() {
        _moviesLiveData.value = Resource.loading()
        viewModelScope.launch {
            try {
                val moviesData = moviesRepository.moviesData()
                val moviesDetail = moviesRepository.moviesDetail()
                val movies = mapToMovie(moviesData, moviesDetail)
                _moviesLiveData.postValue(Resource.success(movies))
            } catch (throwable: Throwable) {
                _moviesLiveData.postValue(Resource.error(throwable))
            }

        }
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