package com.vikiwahyudi.moviecatalogue.mainscreen.movie

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.vikiwahyudi.core.data.Resource
import com.vikiwahyudi.moviecatalogue.ui.MovieAdapter
import com.vikiwahyudi.moviecatalogue.R
import com.vikiwahyudi.moviecatalogue.databinding.FragmentMovieBinding
import com.vikiwahyudi.moviecatalogue.detail.movie.MovieDetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieFragment : Fragment() {

    private val movieViewModel: MovieViewModel by viewModel()

    private var _fragmentMovieBinding: FragmentMovieBinding? = null
    val fragmentMovieBinding get() = _fragmentMovieBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentMovieBinding = FragmentMovieBinding.inflate(layoutInflater, container, false)
        return fragmentMovieBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (activity != null) {
            val movieAdapter = MovieAdapter()
            movieAdapter.onItemClick = { selectedData ->
                val moveIntent = Intent(requireActivity(), MovieDetailActivity::class.java)
                moveIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, selectedData.id)
                startActivity(moveIntent)
            }

            movieViewModel.movies.observe(viewLifecycleOwner) { movies ->
                if (movies != null) {
                    when (movies) {
                        is Resource.Loading -> showProgressBar(true)
                        is Resource.Success -> {
                            showProgressBar(false)
                            movieAdapter.setData(movies.data)
                        }
                        is Resource.Error -> {
                            showProgressBar(false)
                            fragmentMovieBinding.viewError.root.visibility = View.VISIBLE
                            fragmentMovieBinding.viewError.tvError.text =
                                movies.message ?: getString(R.string.something_wrong)
                        }
                    }
                }
            }

            with(fragmentMovieBinding.rvMovie) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = movieAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentMovieBinding = null
    }

    private fun showProgressBar(state: Boolean) {
        fragmentMovieBinding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}