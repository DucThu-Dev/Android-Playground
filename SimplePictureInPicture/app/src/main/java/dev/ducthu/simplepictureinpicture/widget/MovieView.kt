package dev.ducthu.simplepictureinpicture.widget

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.TypedArray
import android.graphics.Color
import android.media.MediaPlayer
import android.transition.TransitionManager
import android.util.AttributeSet
import android.util.Log
import android.view.Surface
import android.view.SurfaceView
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.annotation.RawRes
import dev.ducthu.simplepictureinpicture.R
import java.io.IOException

class MovieView : RelativeLayout {
    companion object {
        class MovieListener {
            fun onMovieStarted() {}

            fun onMovieStopped() {}

            fun onMovieMinimized() {}
        }

        const val TAG = "MovieView"

        const val FAST_FORWARD_REWIND_INTERVAL = 5000

        const val TIMEOUT_CONTROLS = 3000
    }

    private lateinit var mSurfaceView: SurfaceView

    private lateinit var mToggle: ImageButton

    private lateinit var mShade: View

    private lateinit var mFastForward: ImageButton

    private lateinit var mFastRewind: ImageButton

    private lateinit var mMinimize: ImageButton

    private lateinit var mMediaPlayer: MediaPlayer

    @RawRes
    private var mVideoResourceId: Int = 0

    private lateinit var mTitle: String

    private var mAdjustViewBounds: Boolean = false

    private lateinit var mTimeOutHandler: TimeoutHandler

    private lateinit var mMovieListener: MovieListener

    private var mSavedCurrentPosition: Int = 0

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setBackgroundColor(Color.BLACK)
        inflate(context, R.layout.view_movie, this);
        mSurfaceView = findViewById(R.id.surface)
        mShade = findViewById(R.id.shade)
        mToggle = findViewById(R.id.toggle)
        mFastForward = findViewById(R.id.fast_forward)
        mFastRewind = findViewById(R.id.fast_rewind)
        mMinimize = findViewById(R.id.minimize)

        val attributes: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.MovieView,
            defStyleAttr,
            R.style.Widget_PictureInPicture_MovieView
        )

        setVideoR
    }

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setVideoResourceId(@RawRes id: Int) {
        if (id == mVideoResourceId) {
            return;
        }
        mVideoResourceId = id
        var surface = mSurfaceView.holder.surface
        if (surface != null && surface.isValid) {
            closeVideo()
            openVideo(surface)
        }
    }

    fun setAdjustViewBounds(adjustViewBounds: Boolean) {
        if (mAdjustViewBounds == adjustViewBounds) {
            return
        }

        mAdjustViewBounds = adjustViewBounds
        if (adjustViewBounds) {
            background = null
        } else {
            setBackgroundColor(Color.BLACK)
        }

        requestLayout()
    }

    fun showControls() {
        TransitionManager.beginDelayedTransition(this)
        mShade.visibility = View.VISIBLE
        mToggle.visibility = View.VISIBLE
        mFastForward.visibility = View.VISIBLE
        mFastRewind.visibility = View.VISIBLE
        mMinimize.visibility = View.VISIBLE
    }

    fun hideControls() {
        TransitionManager.beginDelayedTransition(this)
        mShade.visibility = View.INVISIBLE
        mToggle.visibility = View.INVISIBLE
        mFastForward.visibility = View.INVISIBLE
        mFastRewind.visibility = View.INVISIBLE
        mMinimize.visibility = View.INVISIBLE
    }

    fun fastForwards() {
        mMediaPlayer.seekTo(mMediaPlayer.currentPosition + FAST_FORWARD_REWIND_INTERVAL)
    }

    fun fastRewind() {
        mMediaPlayer.seekTo(mMediaPlayer.currentPosition - FAST_FORWARD_REWIND_INTERVAL)
    }

    fun setCurrentPosition(): Int {
        return mMediaPlayer.currentPosition
    }

    fun isPlaying(): Boolean {
        return mMediaPlayer.isPlaying
    }

    fun play() {
        mMediaPlayer.start()
        adjustToggleState()
        keepScreenOn = true
        mMovieListener.onMovieStarted()
    }

    fun pause() {
        mMediaPlayer.pause()
        adjustToggleState()
        keepScreenOn = false
        mMovieListener.onMovieStopped()
    }

    fun openVideo(surface: Surface) {
        if (mVideoResourceId == 0) return
        mMediaPlayer = MediaPlayer()
        mMediaPlayer.setSurface(surface)
        startVideo()
    }

    fun startVideo() {
        mMediaPlayer.reset()
        try {
            val fd: AssetFileDescriptor = resources.openRawResourceFd(mVideoResourceId)
            mMediaPlayer.setDataSource(fd)
            mMediaPlayer.setOnPreparedListener { mediaPlayer ->
                requestLayout()
                if (mSavedCurrentPosition > 0) {
                    mediaPlayer.seekTo(mSavedCurrentPosition)
                    mSavedCurrentPosition = 0
                } else {
                    play()
                }
            }
            mMediaPlayer.setOnCompletionListener { mediaPlayer ->
                adjustToggleState()
                keepScreenOn = false
                mMovieListener.onMovieStopped()
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to open video", e)
        }
    }

    fun closeVideo() {
        mMediaPlayer.release()
    }

    fun toggle() {
        if (mMediaPlayer.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun toggleControls() {
        if (mShade.visibility == View.VISIBLE) {
            hideControls()
        } else {
            showControls()
        }
    }

    fun adjustToggleState() {
        if(mMediaPlayer.isPlaying) {
            mToggle.contentDescription = resources.getString(R.string.pause)
            mToggle.setImageResource(R.drawable.)
        }
    }
}