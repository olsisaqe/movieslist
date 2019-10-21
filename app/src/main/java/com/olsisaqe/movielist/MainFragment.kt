package com.olsisaqe.movielist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.olsisaqe.movielist.core.model.ui.Movie
import com.olsisaqe.movielist.model.ui.Resource
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = MoviesAdapter()
        mainRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        mainRecyclerView.adapter = adapter
        swipeToRefresh.setOnRefreshListener { viewModel.refresh() }

        viewModel.moviesLiveData.observe(this, Observer<Resource<List<Movie>>> { movies ->
            when (movies.status) {
                Resource.Status.SUCCESS -> {
                    if (movies.data?.isNotEmpty() == true) {
                        mainRecyclerView.visibility = VISIBLE
                        containerError.visibility = GONE
                        adapter.submitList(movies.data)
                    } else {
                        errorDescription.text = getString(R.string.empty)
                        mainRecyclerView.visibility = GONE
                        containerError.visibility = VISIBLE
                    }
                    swipeToRefresh.isRefreshing = false
                }
                Resource.Status.ERROR -> {
                    errorDescription.text = movies.cause?.localizedMessage
                    mainRecyclerView.visibility = GONE
                    containerError.visibility = VISIBLE
                    swipeToRefresh.isRefreshing = false
                }
                Resource.Status.LOADING -> swipeToRefresh.isRefreshing = true
            }
        })
    }
}