package com.example.moviedb.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviedb.R
import com.example.moviedb.adapters.ReviewAdapter
import com.example.moviedb.databinding.FragmentReviewBinding
import com.example.moviedb.models.Review
import com.example.moviedb.repository.MoviesRepository
import com.example.moviedb.viewmodels.HomeViewModel
import com.example.moviedb.viewmodels.ReviewViewModel
import com.example.moviedb.viewmodels.ViewModelProviderFactory

class ReviewFragment : Fragment() {
    private lateinit var binding: FragmentReviewBinding
    private lateinit var viewModel: ReviewViewModel
    private lateinit var _adapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val movieId = bundle?.getInt("movieId")?.toInt()

        // set up arrow back
        binding.ivBackArrow.setOnClickListener {
            activity?.onBackPressed()
        }
        setUpRecyclerView()

        viewModel = ViewModelProvider(this, ViewModelProviderFactory(MoviesRepository())).get(ReviewViewModel::class.java)
        movieId?.let { viewModel.getListReviews(it) }
        viewModel.reviews.observe(viewLifecycleOwner, Observer { reviews ->
            _adapter.differ.submitList(reviews)
        })
    }

    private fun setUpRecyclerView() {
        _adapter = ReviewAdapter()
        binding.rvReviews.apply {
            adapter = _adapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}