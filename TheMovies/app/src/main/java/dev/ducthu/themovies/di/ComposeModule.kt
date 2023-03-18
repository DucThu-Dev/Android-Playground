package dev.ducthu.themovies.di

import dev.ducthu.themovies.di.annotations.ActivityScope
import dev.ducthu.themovies.di.annotations.FragmentScope

abstract class ComposeModule {
    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun contributeViewModelActivity(): ViewModelActivity

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeViewModelFragment(): ViewModelFragment
}