package dev.ducthu.themovies.model.network

import dev.ducthu.themovies.model.NetworkResponseModel
import dev.ducthu.themovies.model.Video

data class VideoListResponse(
    val id: Int,
    val results: List<Video>
) : NetworkResponseModel