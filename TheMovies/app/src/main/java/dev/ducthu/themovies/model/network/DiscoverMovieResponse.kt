package dev.ducthu.themovies.model.network

import dev.ducthu.themovies.model.NetworkResponseModel
import dev.ducthu.themovies.model.entity.Movie

data class DiscoverMovieResponse(
    val page: Int,
    val results: List<Movie>,
    val total_results: Int,
    val total_pages: Int
) : NetworkResponseModel