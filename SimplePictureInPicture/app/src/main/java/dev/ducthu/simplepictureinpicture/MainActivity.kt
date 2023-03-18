package dev.ducthu.simplepictureinpicture

import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Bundle
import android.util.Rational
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.ducthu.simplepictureinpicture.widget.MovieView

class MainActivity : AppCompatActivity() {

    companion object {
        const val ACTION_MEDIA_CONTROL = "media_control"

        const val EXTRA_CONTROL_TYPE = "control_type"

        const val REQUEST_PLAY = 1

        const val REQUEST_PAUSE = 2

        const val REQUEST_INFO = 3

        const val CONTROL_TYPE_PLAY = 1

        const val CONTROL_TYPE_PAUSE = 2
    }

    val mPictureInPictureParamsBuilder: PictureInPictureParams.Builder =
        PictureInPictureParams.Builder()

    var mMovieView: MovieView? = null

    lateinit var mScrollView: ScrollView

    var mReceiver: BroadcastReceiver? = null

    lateinit var mPlay: String

    lateinit var mPause: String

    private val mOnClickListener: View.OnClickListener = View.OnClickListener { view ->
        if (view.id == R.id.pip) {
            minimize()
        }
    }

    private val mMovieListener = object : MovieView.MovieListener() {
        override fun onMovieStarted() {
            updatePictureInPictureActions(
                R.drawable.ic_pause_24dp, mPause, CONTROL_TYPE_PAUSE, REQUEST_PAUSE
            )
        }

        override fun onMovieStopped() {
            updatePictureInPictureActions(
                R.drawable.ic_play_arrow_24dp, mPlay, CONTROL_TYPE_PLAY, REQUEST_PLAY
            )
        }

        override fun onMovieMinimized() {
            minimize()
        }
    }

    fun updatePictureInPictureActions(
        @DrawableRes iconId: Int, title: String, controlType: Int, requestCode: Int
    ) {
        val actions = mutableListOf<RemoteAction>()

        val intent = PendingIntent.getBroadcast(
            this@MainActivity, requestCode, Intent(
                ACTION_MEDIA_CONTROL
            ).putExtra(EXTRA_CONTROL_TYPE, controlType), 0
        )

        val icon = Icon.createWithResource(this@MainActivity, iconId)
        actions.add(RemoteAction(icon, title, title, intent))
        actions.add(
            RemoteAction(
                Icon.createWithResource(
                    this@MainActivity, R.drawable.ic_info_24dp
                ), resources.getString(R.string.info), resources.getString(
                    R.string.info_description
                ), PendingIntent.getActivity(
                    this@MainActivity, REQUEST_INFO, Intent(
                        Intent.ACTION_VIEW, Uri.parse(getString(R.string.info_uri))
                    ), 0
                )
            )
        )

        mPictureInPictureParamsBuilder.setActions(actions)
        setPictureInPictureParams(mPictureInPictureParamsBuilder.build())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPlay = getString(R.string.play)
        mPause = getString(R.string.pause)

        mMovieView = findViewById(R.id.movie)
        mScrollView = findViewById(R.id.scroll)

        val switchExampleButton: Button = findViewById(R.id.switch_example)
        switchExampleButton.text = getString(R.string.switch_media_session)
        switchExampleButton.setOnClickListener(SwitchActivityOnClick())

        mMovieView?.setMovieListener(mMovieListener)
        findViewById<Button>(R.id.pip).setOnClickListener(mOnClickListener)
    }

    override fun onStop() {
        mMovieView?.pause()
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
        if (!isInPictureInPictureMode) {
            mMovieView?.showControls()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        adjustFullScreen(newConfig)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            adjustFullScreen(resources.configuration)
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean, newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode) {
            mReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    if (intent == null || !ACTION_MEDIA_CONTROL.equals(intent.action)) return

                }
            }

            val controlType = intent.getIntExtra(EXTRA_CONTROL_TYPE, 0)
            when (controlType) {
                CONTROL_TYPE_PLAY -> {
                    mMovieView?.play()
                }
                CONTROL_TYPE_PAUSE -> {
                    mMovieView?.pause()
                }
            }

            registerReceiver(mReceiver, IntentFilter(ACTION_MEDIA_CONTROL))
        } else {
            unregisterReceiver(mReceiver)
            mReceiver = null
            if (mMovieView != null && mMovieView!!.isPlaying()) {
                mMovieView!!.showControls()
            }
        }
    }

    fun minimize() {
        mMovieView?.let {
            it.hideControls()
            val aspectRatio = Rational(it.width, it.height)
            mPictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build()
            enterPictureInPictureMode(mPictureInPictureParamsBuilder.build())
        }
    }

    fun adjustFullScreen(config: Configuration) {
        val insetsController = ViewCompat.getWindowInsetsController(window.decorView)
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            insetsController?.hide(WindowInsetsCompat.Type.systemBars())
            mScrollView.visibility = View.GONE
            mMovieView?.setAdjustViewBounds(false)
        } else {
            insetsController?.show(WindowInsetsCompat.Type.systemBars())
            mScrollView.visibility = View.VISIBLE
            mMovieView?.setAdjustViewBounds(true)
        }
    }

    inner class SwitchActivityOnClick() : View.OnClickListener {
        override fun onClick(v: View?) {
            // do nothing
        }

    }
}