package com.vikiwahyudi.core.domain.usecase.tvshow

import com.vikiwahyudi.core.domain.model.TvShow
import kotlinx.coroutines.flow.Flow

interface TvShowUseCase {
    fun getAllTvShow(): Flow<com.vikiwahyudi.core.data.Resource<List<TvShow>>>
    fun getFavTvShows(): Flow<List<TvShow>>
    fun getDetailTvShow(id: Int): Flow<TvShow>
    fun updateFavoriteTvShow(tvShow: TvShow, state: Boolean)
}