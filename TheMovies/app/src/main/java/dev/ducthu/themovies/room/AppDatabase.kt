package dev.ducthu.themovies.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.skydoves.themovies.room.PeopleDao
import com.skydoves.themovies.room.TvDao
import dev.ducthu.themovies.model.entity.Movie
import dev.ducthu.themovies.model.entity.Person
import dev.ducthu.themovies.model.entity.Tv
import dev.ducthu.themovies.room.convertes.*


@Database(
    entities = [(Movie::class), (Tv::class), (Person::class)],
    version = 3,
    exportSchema = false
)
@TypeConverters(
    value = [(StringListConverter::class), (IntegerListConverter::class),
        (KeywordListConverter::class), (VideoListConverter::class), (ReviewListConverter::class)]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    abstract fun tvDao(): TvDao

    abstract fun peopleDao(): PeopleDao
}