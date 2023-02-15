package com.example.moviedb.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedb.databinding.ItemDetailListMoviesBinding
import com.example.moviedb.databinding.ItemPreviewListMoviesBinding
import com.example.moviedb.models.Result
import com.example.moviedb.util.Constants.Companion.POSTER_BASE_URL

class DetailListAdapter :
    PagingDataAdapter<Result, DetailListAdapter.ViewHolder>(ARTICLE_DIFF_CALLBACK) {

    class ViewHolder(private val binding: ItemDetailListMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(result: Result) {
            Glide.with(binding.ivMoviePoster.context).load(POSTER_BASE_URL + result.poster_path)
                .into(binding.ivMoviePoster)
            binding.tvMovieTitle.text = result.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemDetailListMoviesBinding =
            ItemDetailListMoviesBinding.inflate(inflater, parent, false)
        return ViewHolder(itemDetailListMoviesBinding)
    }

    private var onItemClickListener: ((Result) -> Unit)? = null
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var result = getItem(position)
        if (result != null) {
            holder.onBind(result)
            holder.itemView.apply {
                setOnClickListener {
                    onItemClickListener?.let { it(result) }
                }
            }
        }

    }

    fun setOnItemClickListener(listener: (Result) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val ARTICLE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean =
                oldItem == newItem
        }
    }

}