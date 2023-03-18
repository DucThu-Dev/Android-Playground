package dev.ducthu.themovies.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ComposeModule::class,
        ActivityModule::class,
        ViewModelModule::class,
        NetworkModule::class,
        PersistenceModule::class,
    ]
)
interface AppComponent : AndroidInjector<DaggerApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}