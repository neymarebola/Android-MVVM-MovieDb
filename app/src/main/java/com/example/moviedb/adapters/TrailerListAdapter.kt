package com.example.moviedb.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.moviedb.R
import com.example.moviedb.databinding.ItemSearchListMoviesBinding
import com.example.moviedb.databinding.ItemTrailerLayoutBinding
import com.example.moviedb.models.MovieDetailResponse
import com.example.moviedb.models.MoviesResponse
import com.example.moviedb.models.Result
import com.example.moviedb.models.Video
import com.example.moviedb.util.Constants

class TrailerListAdapter : RecyclerView.Adapter<TrailerListAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemTrailerLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(video: Video) {
            // set up thumbnail video
            Glide.with(binding.ivThumbnailVideo)
                .asBitmap()
                .load("https://img.youtube.com/vi/${video.key}/0.jpg")
                .into(binding.ivThumbnailVideo)
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Video> () {
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemTrailerLayoutBinding = ItemTrailerLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(itemTrailerLayoutBinding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    private var onItemClickListener: ((Video) -> Unit)? = null
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var video = differ.currentList[position]
        holder.onBind(video)
        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let { it(video) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Video) -> Unit) {
        onItemClickListener = listener
    }
}