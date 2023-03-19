package dev.ducthu.themovies.mapper

import dev.ducthu.themovies.model.network.ReviewListResponse

class ReviewResponseMapper : NetworkResponseMapper<ReviewListResponse> {
    override fun onLastPage(response: ReviewListResponse): Boolean {
        return true
    }
}