package com.olsisaqe.movielist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.olsisaqe.movielist.model.api.MoviesData
import com.olsisaqe.movielist.model.ui.Movie
import com.olsisaqe.movielist.model.ui.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.moviesLiveData.observe(this, Observer<Resource<List<Movie>>> {
            fun onChanged(movies: Resource<List<Movie>>?) {
                val movies1 = movies

            }

        })
    }
}
