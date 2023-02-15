package com.example.moviedb.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedb.databinding.ItemReviewLayoutBinding
import com.example.moviedb.databinding.ItemTrailerLayoutBinding
import com.example.moviedb.models.Review
import com.example.moviedb.models.Video

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemReviewLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(review: Review) {
            binding.tvAuthorName.text = review.author
            binding.tvReviewContent.text = review.content
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.author == newItem.author
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemReviewLayoutBinding = ItemReviewLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(itemReviewLayoutBinding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    private var onItemClickListener: ((Review) -> Unit)? = null
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var review = differ.currentList[position]
        holder.onBind(review)
        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let { it(review) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Review) -> Unit) {
        onItemClickListener = listener
    }
}
