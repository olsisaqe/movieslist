package com.olsisaqe.movielist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olsisaqe.movielist.model.ui.Movie
import com.olsisaqe.movielist.model.ui.Resource
import com.olsisaqe.movielist.network.MoviesRepository
import kotlinx.coroutines.launch

class MainViewModel(private val moviesRepository: MoviesRepository): ViewModel() {
    private val movies = MutableLiveData<Resource<List<Movie>>>()
    val moviesLiveData: LiveData<Resource<List<Movie>>> = movies

    init {
        fetchData()
    }

    private fun fetchData() {
        movies.value = Resource.loading()
        viewModelScope.launch {
            val moviesData = moviesRepository.moviesData()
            val moviesDetail = moviesRepository.moviesDetail()
        }
    }
}