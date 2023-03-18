package dev.ducthu.themovies.di

import dagger.Module
import dev.ducthu.themovies.di.annotations.FragmentScope

@Module
abstract class MainActivityFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeMovieListFragment(): MovieListFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeTvListFragment(): TvListFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributePersonListFragment(): PersonListFragment
}