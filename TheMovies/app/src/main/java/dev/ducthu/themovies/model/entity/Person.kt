package dev.ducthu.themovies.model.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.ducthu.themovies.model.network.PersonDetail
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "People")
data class Person(
    @PrimaryKey val id: Int,
    @Embedded var personDetail: PersonDetail? = null,
    var page: Int,
    val profile_path: String?,
    val adult: Boolean,
    val name: String,
    val popularity: Float,
) : Parcelable