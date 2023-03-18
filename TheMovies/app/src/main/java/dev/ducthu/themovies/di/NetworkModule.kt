package dev.ducthu.themovies.di

import androidx.annotation.NonNull
import dagger.Module
import dagger.Provides
import dev.ducthu.themovies.api.*
import dev.ducthu.themovies.api.RequestInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(RequestInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(@NonNull okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().client(okHttpClient)
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideDiscoverService(@NonNull retrofit: Retrofit): TheDiscoverService {
        return retrofit.create(TheDiscoverService::class.java)
    }

    @Provides
    @Singleton
    fun providePeopleService(@NonNull retrofit: Retrofit): PeopleService {
        return retrofit.create(PeopleService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieService(@NonNull retrofit: Retrofit): MovieService {
        return retrofit.create(MovieService::class.java)
    }

    @Provides
    @Singleton
    fun provideTvService(@NonNull retrofit: Retrofit): TvService {
        return retrofit.create(TvService::class.java)
    }
}