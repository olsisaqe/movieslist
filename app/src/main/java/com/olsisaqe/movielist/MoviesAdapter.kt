package com.olsisaqe.movielist

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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

    var expandedPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val context = parent.context
        val item = LayoutInflater.from(context).inflate(R.layout.list_item_movie, parent, false)
        val holder = MoviesViewHolder(item)
        item.setOnClickListener {
            val itemPosition = holder.adapterPosition
            if (getItem(itemPosition).available == false) return@setOnClickListener
            val oldItemPosition = expandedPosition
            expandedPosition = if (itemPosition == expandedPosition) {
                null
            } else {
                itemPosition
            }
            notifyItemChanged(itemPosition)
            if (itemPosition > 0) notifyItemChanged(itemPosition - 1)
            if (itemPosition < itemCount - 1) notifyItemChanged(itemPosition + 1)
            if (oldItemPosition != null && oldItemPosition !in itemPosition - 1..itemPosition + 1) {
                notifyItemChanged(oldItemPosition)
                if (oldItemPosition > 0) notifyItemChanged(oldItemPosition - 1)
                if (oldItemPosition < itemCount - 1) notifyItemChanged(oldItemPosition + 1)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) =
        holder.bind(getItem(position), expandedPosition == position)

    inner class MoviesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(movie: Movie, expanded: Boolean) {
            itemView.title.text = movie.title ?: movie.movieId.toString()
            itemView.subtitle.text = movie.subtitle
            itemView.movieCardView.isEnabled = movie.available == true
            itemView.movieCardView.foreground = if (movie.available == true) {
                null
            } else {
                ColorDrawable(itemView.resources.getColor(R.color.movie_unavailable_overlay))
            }
            if (movie.image != null && URLUtil.isValidUrl(movie.image)) {
                Glide
                    .with(itemView)
                    .load(movie.image)
                    .centerCrop()
                    .into(itemView.thumbnail)
            }
            itemView.price.text = itemView.resources.getString(R.string.price, movie.price)
            itemView.price.visibility = if (expanded) VISIBLE else GONE
        }
    }
}