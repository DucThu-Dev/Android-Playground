package dev.ducthu.themovies.model.network

data class ErrorEnvelope(
    val status_code: Int,
    val status_message: String,
    val success: Boolean
)