package com.vikiwahyudi.core.data

import com.vikiwahyudi.core.data.source.remote.network.ApiResponse
import com.vikiwahyudi.core.data.source.remote.response.movie.MovieResponse
import com.vikiwahyudi.core.domain.model.Movie
import com.vikiwahyudi.core.domain.repository.IMovieRepository
import com.vikiwahyudi.core.utils.AppExecutors
import com.vikiwahyudi.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepository(
    private val remoteDataSource: com.vikiwahyudi.core.data.source.remote.RemoteDataSource,
    private val localDataSource: com.vikiwahyudi.core.data.source.local.LocalDataSource,
    private val appExecutors: AppExecutors
) : IMovieRepository {
    override fun getAllMovie(): Flow<Resource<List<Movie>>> =
        object :
            com.vikiwahyudi.core.data.NetworkBoundResource<List<Movie>, List<MovieResponse>>() {
            override fun loadFromDB(): Flow<List<Movie>> {
                return localDataSource.getAllMovie().map {
                    DataMapper.mapMovieEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Movie>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<MovieResponse>>> =
                remoteDataSource.getMovies()

            override suspend fun saveCallResult(data: List<MovieResponse>) {
                val movieList = DataMapper.mapMovieResponsesToEntities(data)
                localDataSource.insertMovies(movieList)
            }
        }.asFlow()

    override fun getFavMovies(): Flow<List<Movie>> {
        return localDataSource.getFavMovies().map {
            DataMapper.mapMovieEntitiesToDomain(it)
        }
    }

    override fun getDetailMovie(id: Int): Flow<Movie> {
        return localDataSource.getDetailMovie(id).map {
            DataMapper.mapMovieEntityToDomain(it)
        }
    }

    override fun updateFavoriteMovie(movie: Movie, state: Boolean) {
        val movieEntity = DataMapper.mapMovieDomainToEntity(movie)
        appExecutors.diskIO().execute {
            localDataSource.updateFavoriteMovie(movieEntity, state)
        }
    }
}