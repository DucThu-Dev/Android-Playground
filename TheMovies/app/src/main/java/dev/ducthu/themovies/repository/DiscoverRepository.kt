package dev.ducthu.themovies.repository

import androidx.lifecycle.LiveData
import dev.ducthu.themovies.api.ApiResponse
import dev.ducthu.themovies.api.TheDiscoverService
import dev.ducthu.themovies.mapper.MovieResponseMapper
import dev.ducthu.themovies.mapper.TvResponseMapper
import dev.ducthu.themovies.model.Resource
import dev.ducthu.themovies.model.entity.Movie
import dev.ducthu.themovies.model.entity.Tv
import dev.ducthu.themovies.model.network.DiscoverMovieResponse
import dev.ducthu.themovies.model.network.DiscoverTvResponse
import dev.ducthu.themovies.room.MovieDao
import dev.ducthu.themovies.room.TvDao
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoverRepository @Inject constructor(
    val discoverService: TheDiscoverService, val movieDao: MovieDao, val tvDao: TvDao
) : Repository {

    init {
        Timber.d("Injection DiscoverRepository")
    }

    fun loadMovies(page: Int): LiveData<Resource<List<Movie>>> {
        return object :
            NetworkBoundRepository<List<Movie>, DiscoverMovieResponse, MovieResponseMapper>() {
            override fun shouldFetch(data: List<Movie>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun loadFromDb(): LiveData<List<Movie>> {
                return movieDao.getMovieList(page_ = page)
            }

            override fun fetchService(): LiveData<ApiResponse<DiscoverMovieResponse>> {
                return discoverService.fetchDiscoverMovie(page)
            }

            override fun mapper(): MovieResponseMapper {
                return MovieResponseMapper()
            }

            override fun onFetchFailed(message: String?) {
                Timber.d("onFetchFail $message")
            }

            override fun saveFetchData(items: DiscoverMovieResponse) {
                TODO("Not yet implemented")
            }
        }.asLiveData()
    }

    fun loadTvs(page: Int): LiveData<Resource<List<Tv>>> {
        return object : NetworkBoundRepository<List<Tv>, DiscoverTvResponse, TvResponseMapper>() {
            override fun shouldFetch(data: List<Tv>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun loadFromDb(): LiveData<List<Tv>> {
                return tvDao.getTvList(page_ = page)
            }

            override fun fetchService(): LiveData<ApiResponse<DiscoverTvResponse>> {
                return discoverService.fetchDiscoverTv(page)
            }

            override fun mapper(): TvResponseMapper {
                return TvResponseMapper()
            }

            override fun onFetchFailed(message: String?) {
                Timber.d("onFetchFailed $message")
            }

            override fun saveFetchData(items: DiscoverTvResponse) {
                for (item in items.results) {
                    item.page = page
                }
                tvDao.insertTv(tvs = items.results)
            }
        }.asLiveData()
    }
}