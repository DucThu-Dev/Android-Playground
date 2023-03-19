package dev.ducthu.themovies.mapper

import dev.ducthu.themovies.model.network.DiscoverMovieResponse
import timber.log.Timber

class MovieResponseMapper : NetworkResponseMapper<DiscoverMovieResponse> {
    override fun onLastPage(response: DiscoverMovieResponse): Boolean {
        Timber.d("loadPage : ${response.page}/${response.total_pages}")
        return response.page > response.total_pages
    }
}
