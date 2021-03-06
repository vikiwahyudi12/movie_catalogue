package com.vikiwahyudi.moviecatalogue.detail.tvshow

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.vikiwahyudi.core.domain.model.TvShow
import com.vikiwahyudi.core.utils.NetworkInfo.IMAGE_URL
import com.vikiwahyudi.moviecatalogue.R
import com.vikiwahyudi.moviecatalogue.databinding.ActivityTvShowDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TvShowDetailActivity : AppCompatActivity() {

    private val tvShowDetailViewModel: TvShowDetailViewModel by viewModel()

    private var _activityTvShowDetailBinding: ActivityTvShowDetailBinding? = null
    val activityTvShowDetailBinding get() = _activityTvShowDetailBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityTvShowDetailBinding = ActivityTvShowDetailBinding.inflate(layoutInflater)
        setContentView(activityTvShowDetailBinding.root)
        supportActionBar?.title = "TV Show Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tvShowId = intent.getIntExtra(EXTRA_TV_SHOW_ID, 0)

        tvShowDetailViewModel.getTvShowDetail(tvShowId).observe(this) { tvShow ->
            populatesTvShow(tvShow)
        }
    }

    private fun populatesTvShow(tvShow: TvShow) {
        with(activityTvShowDetailBinding.detailContent) {
            textTitle.text = tvShow.name
            textRelease.text = tvShow.firstAirDate
            textTvShowOverview.text = tvShow.overview
            textTvShowScore.text = getString(R.string.user_score, tvShow.voteAverage.toString())
            textTvShowPopularity.text = getString(R.string.popularity, tvShow.popularity.toString())
            Glide.with(activityTvShowDetailBinding.detailContent.root)
                .load(IMAGE_URL + tvShow.posterPath)
                .transform(RoundedCorners(20))
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                )
                .into(imagePoster)

            var statusFavorite = tvShow.isFav
            setStatusFavorite(statusFavorite)

            btnFavTvShow.setOnClickListener {
                statusFavorite = !statusFavorite
                tvShowDetailViewModel.updateFavoriteTvShow(tvShow, statusFavorite)
                Toast.makeText(
                    this@TvShowDetailActivity,
                    "Berhasil!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        with(activityTvShowDetailBinding.detailContent) {
            if (statusFavorite) {
                val favIcon: Drawable? =
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_delete_forever_24, theme)
                btnFavTvShow.setCompoundDrawablesWithIntrinsicBounds(favIcon, null, null, null)
                btnFavTvShow.text = getString(R.string.delete_favorite)
            } else {
                val favIcon: Drawable? =
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_favorite_24, theme)
                btnFavTvShow.setCompoundDrawablesWithIntrinsicBounds(favIcon, null, null, null)
                btnFavTvShow.text = getString(R.string.add_favorite)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityTvShowDetailBinding = null
    }

    companion object {
        const val EXTRA_TV_SHOW_ID = "extra_tv_show_id"
    }
}