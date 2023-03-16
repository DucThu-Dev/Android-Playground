package dev.ducthu.simplepictureinpicture

import android.app.PictureInPictureParams
import android.content.BroadcastReceiver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ScrollView

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

    lateinit var mScrollView: ScrollView

    lateinit var mReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}