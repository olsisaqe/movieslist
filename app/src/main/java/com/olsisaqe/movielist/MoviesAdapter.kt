package com.olsisaqe.movielist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.olsisaqe.movielist.model.ui.Movie
import kotlinx.android.synthetic.main.list_item_movie.view.*

class MoviesAdapter : ListAdapter<Movie, MoviesAdapter.MoviesViewHolder>(DIFF) {
    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem.movieId == newItem.movieId

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val context = parent.context
        val item = LayoutInflater.from(context).inflate(R.layout.list_item_movie, parent, false)
        return MoviesViewHolder(item)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class MoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(movie: Movie) {
            itemView.title.text = movie.title ?: movie.movieId.toString()
            itemView.subtitle.text = movie.subtitle
            if (movie.image != null && URLUtil.isValidUrl(movie.image)) {
                Glide
                    .with(itemView)
                    .load(movie.image)
                    .centerCrop()
                    .into(itemView.thumbnail)
            }
        }
    }
}