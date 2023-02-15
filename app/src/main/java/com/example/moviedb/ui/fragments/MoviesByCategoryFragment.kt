package com.example.moviedb.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviedb.R
import com.example.moviedb.adapters.DetailListAdapter
import com.example.moviedb.databinding.FragmentMoviesByCategoryBinding
import com.example.moviedb.repository.MoviesRepository
import com.example.moviedb.util.Constants
import com.example.moviedb.viewmodels.MoviesByCategoryViewModel
import com.example.moviedb.viewmodels.ViewModelProviderFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MoviesByCategoryFragment : Fragment() {
    private lateinit var binding: FragmentMoviesByCategoryBinding
    private lateinit var viewModel: MoviesByCategoryViewModel
    private lateinit var _adapter: DetailListAdapter
    private var catId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater,
                R.layout.fragment_movies_by_category,
                container,
                false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        setUpRecyclerView()

        val bundle = arguments
        catId = bundle?.getInt("movie_category_id")
        setUpToolbarTitle()
        setUpView()
    }

    private fun setUpToolbarTitle() {
        //  set up arrow back
        binding.ivBackArrow.setOnClickListener {
            activity?.onBackPressed()
        }

        if (catId == Constants.POPULAR) {
            binding.tvCategoryName.text = "Popular"
        } else if (catId == Constants.TOP_RATED) {
            binding.tvCategoryName.text = "Top rated"
        } else {
            binding.tvCategoryName.text = "Upcoming"
        }
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this,
            ViewModelProviderFactory(MoviesRepository())).get(MoviesByCategoryViewModel::class.java)
    }

    private fun setUpRecyclerView() {
        _adapter = DetailListAdapter()
        val gridLayoutManager = GridLayoutManager(context, 2)
        binding.rvListMovies.apply {
            adapter = _adapter
            layoutManager = gridLayoutManager
        }

        _adapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie", it)
            }
            findNavController().navigate(
                R.id.action_moviesByCategoryFragment_to_detailMovieFragment, bundle
            )
        }
    }

    private fun setUpView() {
        lifecycleScope.launch(Dispatchers.IO) {
            if (catId == Constants.POPULAR) {
                viewModel.allPopularMovies.collect {
                    _adapter.submitData(it)
                }
            } else if (catId == Constants.TOP_RATED) {
                viewModel.allTopRatedMovies.collect {
                    _adapter.submitData(it)
                }
            } else {
                viewModel.allUpComingMovies.collect {
                    _adapter.submitData(it)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                _adapter.loadStateFlow.collect {
                    binding.prgBar.isVisible = it.source.prepend is LoadState.Loading
                    binding.prgBar.isVisible = it.source.append is LoadState.Loading
                }
            }
        }
    }
}