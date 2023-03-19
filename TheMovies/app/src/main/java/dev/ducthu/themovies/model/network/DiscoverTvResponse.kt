package dev.ducthu.themovies.model.network

import dev.ducthu.themovies.model.NetworkResponseModel
import dev.ducthu.themovies.model.entity.Tv

data class DiscoverTvResponse(
    val page: Int,
    val results: List<Tv>,
    val total_results: Int,
    val total_pages: Int
) : NetworkResponseModel