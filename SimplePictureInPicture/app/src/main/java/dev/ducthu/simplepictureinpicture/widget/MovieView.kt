package dev.ducthu.simplepictureinpicture.widget

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.TypedArray
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.os.Message
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
import java.lang.ref.WeakReference

class MovieView : RelativeLayout {
    companion object {
        class MovieListener {
            fun onMovieStarted() {}

            fun onMovieStopped() {}

            fun onMovieMinimized() {}
        }

        class TimeoutHandler(view: MovieView) : Handler(Looper.getMainLooper()) {

            companion object {
                const val MESSAGE_HIDE_CONTROLS = 1
            }

            private val mMovieViewRef: WeakReference<MovieView>

            override fun handleMessage(msg: Message) {
                if (msg.what == MESSAGE_HIDE_CONTROLS) {
                    mMovieViewRef.get()?.hideControls()
                } else {
                    super.handleMessage(msg)
                }
            }

            init {
                mMovieViewRef = WeakReference(view)
            }
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

    private var mMediaPlayer: MediaPlayer? = null

    @RawRes
    private var mVideoResourceId: Int = 0

    private lateinit var mTitle: String

    private var mAdjustViewBounds: Boolean = false

    private var mTimeOutHandler: TimeoutHandler? = null

    private var mMovieListener: MovieListener? = null

    private var mSavedCurrentPosition: Int = 0

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
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
            attrs, R.styleable.MovieView, defStyleAttr, R.style.Widget_PictureInPicture_MovieView
        )


    }

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val videoWidth = mMediaPlayer?.videoWidth ?: 0
        val videoHeight = mMediaPlayer?.videoHeight ?: 0
        if (videoWidth != 0 && videoHeight != 0) {
            val aspectRatio: Float = videoHeight.toFloat() / videoWidth.toFloat()
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = MeasureSpec.getSize(heightMeasureSpec)
            val widthMode = MeasureSpec.getMode(widthMeasureSpec)
            val heightMode = MeasureSpec.getMode(heightMeasureSpec)
            if (mAdjustViewBounds) {
                if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
                    super.onMeasure(
                        widthMeasureSpec,
                        MeasureSpec.makeMeasureSpec(
                            (width * aspectRatio).toInt(),
                            MeasureSpec.EXACTLY
                        )
                    )
                } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
                    super.onMeasure(
                        MeasureSpec.makeMeasureSpec(
                            (height / aspectRatio).toInt(),
                            heightMeasureSpec
                        ),
                        heightMeasureSpec
                    )
                } else {
                    super.onMeasure(
                        widthMeasureSpec,
                        MeasureSpec.makeMeasureSpec(
                            (width * aspectRatio).toInt(),
                            MeasureSpec.EXACTLY
                        )
                    )
                }
            } else {
                val viewRatio: Float = (height / width).toFloat()
                if (aspectRatio > viewRatio) {
                    val padding = ((width - height / aspectRatio) / 2).toInt()
                    setPadding(padding, 0, padding, 0)
                } else {
                    val padding = ((height - width * aspectRatio) / 2).toInt()
                    setPadding(0, padding, 0, padding)
                }

                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }

            return
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDetachedFromWindow() {
        mTimeOutHandler?.let {
            it.removeMessages(TimeoutHandler.MESSAGE_HIDE_CONTROLS)
            mTimeOutHandler = null
        }
        super.onDetachedFromWindow()
    }

    fun setMovieListener(movieListener: MovieListener?) {
        mMovieListener = movieListener
    }

    fun setTitle(title: String) {
        mTitle = title
    }

    fun getTitle(): String = mTitle

    fun getVideoResourceId(): Int = mVideoResourceId

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
        mMediaPlayer?.let {
            it.seekTo(
                it.currentPosition + FAST_FORWARD_REWIND_INTERVAL
            )
        }
    }

    fun fastRewind() {
        mMediaPlayer?.let { it.seekTo(it.currentPosition - FAST_FORWARD_REWIND_INTERVAL) }
    }

    fun setCurrentPosition(): Int {
        return mMediaPlayer?.currentPosition ?: 0
    }

    fun isPlaying(): Boolean {
        return mMediaPlayer?.isPlaying ?: false
    }

    fun play() {
        mMediaPlayer?.let {
            it.start()
            adjustToggleState()
            keepScreenOn = true
            mMovieListener.onMovieStarted()
        }
    }

    fun pause() {
        mMediaPlayer?.let {
            it.pause()
            adjustToggleState()
            keepScreenOn = false
            mMovieListener.onMovieStopped()
        }
    }

    fun openVideo(surface: Surface) {
        if (mVideoResourceId == 0) return
        mMediaPlayer = MediaPlayer()
        mMediaPlayer?.setSurface(surface)
        startVideo()
    }

    fun startVideo() {
        mMediaPlayer?.reset()
        try {
            val fd: AssetFileDescriptor = resources.openRawResourceFd(mVideoResourceId)
            mMediaPlayer?.setDataSource(fd)
            mMediaPlayer?.setOnPreparedListener { mediaPlayer ->
                requestLayout()
                if (mSavedCurrentPosition > 0) {
                    mediaPlayer.seekTo(mSavedCurrentPosition)
                    mSavedCurrentPosition = 0
                } else {
                    play()
                }
            }
            mMediaPlayer?.setOnCompletionListener { mediaPlayer ->
                adjustToggleState()
                keepScreenOn = false
                mMovieListener.onMovieStopped()
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to open video", e)
        }
    }

    fun closeVideo() {
        mMediaPlayer?.release()
        mMediaPlayer = null
    }

    fun toggle() {
        if (mMediaPlayer?.isPlaying == true) {
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
        if (mMediaPlayer != null && mMediaPlayer?.isPlaying == true) {
            mToggle.contentDescription = resources.getString(R.string.pause)
            mToggle.setImageResource(R.drawable.ic_pause_64dp)
        } else {
            mToggle.contentDescription = resources.getString(R.string.play)
            mToggle.setImageResource(R.drawable.ic_play_arrow_64dp)
        }
    }
}