package dev.ducthu.themovies.compose

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

abstract class ViewModelActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState, persistentState)
    }
}