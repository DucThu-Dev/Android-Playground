package dev.ducthu.themovies.di

import dev.ducthu.themovies.di.annotations.ActivityScope
import dev.ducthu.themovies.view.MainActivity

abstract class ActivityModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityFragmentModule::class])
}