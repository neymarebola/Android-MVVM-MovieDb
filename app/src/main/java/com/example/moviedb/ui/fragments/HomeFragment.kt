package com.example.moviedb.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.adapters.PreviewListAdapter
import com.example.moviedb.databinding.FragmentHomeBinding
import com.example.moviedb.repository.MoviesRepository
import com.example.moviedb.util.Constants
import com.example.moviedb.viewmodels.HomeViewModel
import com.example.moviedb.viewmodels.ViewModelProviderFactory

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var popularPreviewListAdapter: PreviewListAdapter
    private lateinit var topRatedPreviewListAdapter: PreviewListAdapter
    private lateinit var upComingPreviewListAdapter: PreviewListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelProviderFactory = ViewModelProviderFactory(MoviesRepository())
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(HomeViewModel::class.java)
        setUpPopularPreviewList()
        setUpTopRatedPreviewList()
        setUpUpComingPreviewList()

        val controller = findNavController()
        // an vao nut all chuyen sang man hinh list movies
        val popularBundle = Bundle().apply {
            putInt("movie_category_id", Constants.POPULAR)
        }
        binding.btnAllPopular.setOnClickListener {
            controller.navigate(R.id.moviesByCategoryFragment, popularBundle)
        }

        val topRatedBundle = Bundle().apply {
            putInt("movie_category_id", Constants.TOP_RATED)
        }
        binding.btnAllTopRated.setOnClickListener {
            controller.navigate(R.id.moviesByCategoryFragment, topRatedBundle)
        }

        val upComingBundle = Bundle().apply {
            putInt("movie_category_id", Constants.UP_COMING)
        }
        binding.btnAllUpComing.setOnClickListener {
            controller.navigate(R.id.moviesByCategoryFragment, upComingBundle)
        }

        // click search button
        binding.btnSearch.setOnClickListener {
            controller.navigate(R.id.searchFragment)
        }

    }

    private fun setUpPopularPreviewList() {
        popularPreviewListAdapter = PreviewListAdapter()
        setUpRecyclerView(binding.rvPreviewPopularList, popularPreviewListAdapter)
        viewModel.topTenPopularMovies.observe(viewLifecycleOwner, Observer { listResult ->
            popularPreviewListAdapter.differ.submitList(listResult)
        })

        popularPreviewListAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie", it)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_detailMovieFragment, bundle
            )
        }

    }

    private fun setUpTopRatedPreviewList() {
        topRatedPreviewListAdapter = PreviewListAdapter()
        setUpRecyclerView(binding.rvPreviewTopRatedList, topRatedPreviewListAdapter)
        viewModel.topTenTopRatedMovies.observe(viewLifecycleOwner, Observer { listResult ->
            topRatedPreviewListAdapter.differ.submitList(listResult)
        })

        topRatedPreviewListAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie", it)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_detailMovieFragment, bundle
            )
        }
    }

    private fun setUpUpComingPreviewList() {
        upComingPreviewListAdapter = PreviewListAdapter()
        setUpRecyclerView(binding.rvPreviewUpComingList, upComingPreviewListAdapter)
        viewModel.topTenUpComingMovies.observe(viewLifecycleOwner, Observer { listResult ->
            upComingPreviewListAdapter.differ.submitList(listResult)
        })

        upComingPreviewListAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie", it)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_detailMovieFragment, bundle
            )
        }
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView, prevAdapter: PreviewListAdapter) {
        recyclerView.apply {
            adapter = prevAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

}