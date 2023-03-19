package dev.ducthu.themovies.model.network

import dev.ducthu.themovies.model.NetworkResponseModel
import dev.ducthu.themovies.model.Review

class ReviewListResponse(
    val id: Int,
    val page: Int,
    val results: List<Review>,
    val total_pages: Int,
    val total_results: Int
) : NetworkResponseModel
