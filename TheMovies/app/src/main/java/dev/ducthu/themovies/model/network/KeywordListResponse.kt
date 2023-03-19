package dev.ducthu.themovies.model.network

import dev.ducthu.themovies.model.Keyword
import dev.ducthu.themovies.model.NetworkResponseModel

data class KeywordListResponse(
    val id: Int,
    val keywords: List<Keyword>
) : NetworkResponseModel