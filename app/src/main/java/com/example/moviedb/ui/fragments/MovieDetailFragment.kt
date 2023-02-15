package com.example.moviedb.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviedb.R
import com.example.moviedb.adapters.SimilarListAdapter
import com.example.moviedb.adapters.TrailerListAdapter
import com.example.moviedb.databinding.FragmentDetailMovieBinding
import com.example.moviedb.models.MovieDetailResponse
import com.example.moviedb.models.Result
import com.example.moviedb.repository.MoviesRepository
import com.example.moviedb.util.Constants
import com.example.moviedb.viewmodels.MovieDetailViewModel
import com.example.moviedb.viewmodels.ViewModelProviderFactory

class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailMovieBinding
    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var videoAdapter: TrailerListAdapter
    private lateinit var similarMovieAdapter: SimilarListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail_movie, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get id of movie
        val bundle = arguments
        val movie = bundle?.getSerializable("movie") as Result

        setUpViewModel()
        setUpVideosRecyclerView()
        setUpSimilarMoviesRecyclerView()

        viewModel.getMovieDetail(movie.id)
        viewModel.movieDetail.observe(viewLifecycleOwner, Observer { movieDetailResponse ->
            setUpView(movieDetailResponse)
            videoAdapter.differ.submitList(movieDetailResponse.videos.videos)
        })

        viewModel.getSimilarMovies(movie.id)
        viewModel.similarMovies.observe(viewLifecycleOwner, Observer { similarMovies ->
            similarMovieAdapter.differ.submitList(similarMovies.results)
        })
    }

    private fun setUpViewModel() {
        val viewModelProviderFactory = ViewModelProviderFactory(MoviesRepository())
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MovieDetailViewModel::class.java)
    }

    private fun setUpView(movie: MovieDetailResponse) {
        //  set up arrow back
        binding.ivBackArrow.setOnClickListener {
            activity?.onBackPressed()
        }

        // set up title
        binding.tvMovieTitle.text = movie.title
        // set up poster
        context?.let {
            Glide.with(it).load(Constants.POSTER_BASE_URL + movie.poster_path)
                .into(binding.imgPoster)
        }
        // set up original title
        binding.tvOriginalTitle.text = movie.original_title
        // set up release date
        binding.tvReleaseDate.text =
            "${getReleaseYear(movie.release_date)}, ${movie.original_language}"
        // set up point
        binding.tvVoteAverage.text = movie.vote_average.toString()
        // set up status
        binding.tvStatus.text = movie.status
        // set up revenue
        binding.tvRevenue.text = movie.revenue.toString()
        // set up overview
        binding.tvOverview.text = movie.overview

        // click on read all review
        binding.btnReadReviews.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("movieId", movie.id)
            }
            findNavController().navigate(
                R.id.action_detailMovieFragment_to_reviewFragment, bundle
            )
        }
    }

    private fun setUpVideosRecyclerView() {
        videoAdapter = TrailerListAdapter()
        binding.rvVideos.apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        videoAdapter.setOnItemClickListener {video ->
            viewModel.openYoutube(video)
            viewModel.navigateToYoutube.observe(viewLifecycleOwner, Observer { uri ->
                openYoutube(uri)
            })
        }
    }

    private fun setUpSimilarMoviesRecyclerView() {
        similarMovieAdapter = SimilarListAdapter()
        binding.rvSimilarMovies.apply {
            adapter = similarMovieAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        similarMovieAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie", it)
            }
            findNavController().navigate(
                R.id.action_detailMovieFragment_self, bundle
            )
        }
    }

    private fun openYoutube(uri : String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.`package` = "com.google.android.youtube"
        startActivity(intent)
    }

    private fun getReleaseYear(releaseDate: String): CharSequence = releaseDate.subSequence(0, 4)
}