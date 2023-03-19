package dev.ducthu.themovies.mapper

import dev.ducthu.themovies.model.network.VideoListResponse


class VideoResponseMapper : NetworkResponseMapper<VideoListResponse> {
    override fun onLastPage(response: VideoListResponse): Boolean {
        return true
    }
}