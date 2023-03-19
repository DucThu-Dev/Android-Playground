package dev.ducthu.themovies.mapper

import dev.ducthu.themovies.model.NetworkResponseModel

interface NetworkResponseMapper<in FROM : NetworkResponseModel> {
    fun onLastPage(response: FROM): Boolean
}