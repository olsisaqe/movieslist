package com.olsisaqe.movielist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olsisaqe.movielist.core.model.ui.Movie
import com.olsisaqe.movielist.core.network.MoviesService
import com.olsisaqe.movielist.model.ui.Resource
import kotlinx.coroutines.launch

class MainViewModel(private val moviesService: MoviesService) : ViewModel() {
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
                val movies = moviesService.getMovies()
                _moviesLiveData.postValue(Resource.success(movies))
            } catch (throwable: Throwable) {
                _moviesLiveData.postValue(Resource.error(throwable))
            }

        }
    }
}