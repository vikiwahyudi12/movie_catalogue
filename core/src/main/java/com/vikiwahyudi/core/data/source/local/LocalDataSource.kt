package com.vikiwahyudi.core.data.source.local

import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val movieDBAppDao: com.vikiwahyudi.core.data.source.local.room.MovieDBAppDao) {

    fun getAllMovie(): Flow<List<com.vikiwahyudi.core.data.source.local.entity.MovieEntity>> =
        movieDBAppDao.getAllMovie()

    fun getAllTvShow(): Flow<List<com.vikiwahyudi.core.data.source.local.entity.TvShowEntity>> =
        movieDBAppDao.getAllTvShow()

    fun getFavMovies(): Flow<List<com.vikiwahyudi.core.data.source.local.entity.MovieEntity>> =
        movieDBAppDao.getFavMovies()

    fun getFavTvShows(): Flow<List<com.vikiwahyudi.core.data.source.local.entity.TvShowEntity>> =
        movieDBAppDao.getFavTvShows()

    suspend fun insertMovies(movies: List<com.vikiwahyudi.core.data.source.local.entity.MovieEntity>) =
        movieDBAppDao.insertMovies(movies)

    suspend fun insertTvShows(tvShows: List<com.vikiwahyudi.core.data.source.local.entity.TvShowEntity>) =
        movieDBAppDao.insertTvShows(tvShows)

    fun getDetailMovie(id: Int): Flow<com.vikiwahyudi.core.data.source.local.entity.MovieEntity> =
        movieDBAppDao.getDetailMovie(id)

    fun getDetailTvShow(id: Int): Flow<com.vikiwahyudi.core.data.source.local.entity.TvShowEntity> =
        movieDBAppDao.getDetailTvShow(id)

    fun updateFavoriteMovie(
        movie: com.vikiwahyudi.core.data.source.local.entity.MovieEntity,
        newState: Boolean
    ) {
        movie.isFav = newState
        movieDBAppDao.updateFavoriteMovie(movie)
    }

    fun updateFavoriteTvShow(
        tvShow: com.vikiwahyudi.core.data.source.local.entity.TvShowEntity,
        newState: Boolean
    ) {
        tvShow.isFav = newState
        movieDBAppDao.updateFavoriteTvShow(tvShow)
    }
}