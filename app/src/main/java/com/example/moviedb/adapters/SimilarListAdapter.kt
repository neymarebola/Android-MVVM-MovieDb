package com.example.moviedb.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedb.databinding.ItemSimilarMovieBinding
import com.example.moviedb.databinding.ItemTrailerLayoutBinding
import com.example.moviedb.models.Result
import com.example.moviedb.models.Video
import com.example.moviedb.util.Constants

class SimilarListAdapter : RecyclerView.Adapter<SimilarListAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemSimilarMovieBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(result: Result) {
            // load poster
            Glide.with(binding.root).load(Constants.POSTER_BASE_URL + result.poster_path)
                .into(binding.ivMoviePoster)
            // load title
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
        val itemSimilarLayoutBinding = ItemSimilarMovieBinding.inflate(inflater, parent, false)
        return ViewHolder(itemSimilarLayoutBinding)
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