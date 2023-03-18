package dev.ducthu.themovies.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    val id: String,
    val author: String,
    val content: String,
    val url: String
) : Parcelable