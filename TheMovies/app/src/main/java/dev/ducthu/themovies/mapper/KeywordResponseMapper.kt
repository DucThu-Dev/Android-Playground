package dev.ducthu.themovies.mapper

import dev.ducthu.themovies.model.network.KeywordListResponse

class KeywordResponseMapper : NetworkResponseMapper<KeywordListResponse> {
    override fun onLastPage(response: KeywordListResponse): Boolean {
        return true
    }
}