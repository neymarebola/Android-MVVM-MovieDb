package com.example.moviedb.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedb.databinding.ItemPreviewListMoviesBinding
import com.example.moviedb.databinding.ItemSearchListMoviesBinding
import com.example.moviedb.models.Result
import com.example.moviedb.util.Constants.Companion.POSTER_BASE_URL

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemSearchListMoviesBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(result: Result) {
            Glide.with(binding.ivMoviePoster.context).load(POSTER_BASE_URL + result.poster_path).into(binding.ivMoviePoster)
            binding.tvMovieTitle.text = result.title
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Result> () {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemSearchListMoviesBinding = ItemSearchListMoviesBinding.inflate(inflater, parent, false)
        return ViewHolder(itemSearchListMoviesBinding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    private var onItemClickListener: ((Result) -> Unit)? = null
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var result = differ.currentList[position]
        holder.onBind(result)
        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let { it(result) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Result) -> Unit) {
        onItemClickListener = listener
    }

}