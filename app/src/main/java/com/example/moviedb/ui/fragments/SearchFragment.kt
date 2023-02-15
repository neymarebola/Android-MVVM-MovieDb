package com.example.moviedb.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviedb.R
import com.example.moviedb.adapters.SearchAdapter
import com.example.moviedb.databinding.FragmentSearchBinding
import com.example.moviedb.repository.MoviesRepository
import com.example.moviedb.viewmodels.HomeViewModel
import com.example.moviedb.viewmodels.SearchViewModel
import com.example.moviedb.viewmodels.ViewModelProviderFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  set up arrow back
        binding.ivBackArrow.setOnClickListener {
            activity?.onBackPressed()
        }
        setUpRecyclerView()

        val viewModelProviderFactory = ViewModelProviderFactory(MoviesRepository())
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(SearchViewModel::class.java)

        viewModel.searchMovies.observe(viewLifecycleOwner, Observer { it ->
            searchAdapter.differ.submitList(it)
        })

        var job: Job? = null
        binding.editSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.getSearchMovies(editable.toString())
                    }
                }
            }
        }

        // click on remove icon
        binding.editSearch.setOnTouchListener { view, motionEvent ->
            val DRAWBLE_RIGHT = 2
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                if (motionEvent.rawX >= binding.editSearch.right - binding.editSearch.compoundDrawables[DRAWBLE_RIGHT].bounds.width()) {
                    binding.editSearch.text = null
                    searchAdapter.differ.submitList(emptyList())
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }

    }

    private fun setUpRecyclerView() {
        searchAdapter = SearchAdapter()
        binding.rvSearchMovies.apply {
            adapter = searchAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        searchAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie", it)
            }
            findNavController().navigate(
                R.id.action_searchFragment_to_detailMovieFragment, bundle
            )
        }
    }
}