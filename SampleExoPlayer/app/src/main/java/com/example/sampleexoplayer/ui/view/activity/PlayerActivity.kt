package com.example.sampleexoplayer.ui.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.sampleexoplayer.databinding.ActivityPlayerBinding
import com.example.sampleexoplayer.device.utility.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.runBlocking
import kotlin.math.abs

class PlayerActivity : AppCompatActivity(),
    GestureDetector.OnGestureListener, AudioManager.OnAudioFocusChangeListener {

    private lateinit var _binding: ActivityPlayerBinding
    private val binding get() = _binding

    private var url: String = ""
    private var videoQuality = 0
    private var audioQuality = 0

    private var player: ExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playBackPosition: Long = 0

    private var audioManager: AudioManager? = null
    private lateinit var gestureDetectorCompat: GestureDetectorCompat
    private var brightness = 0
    private var volume = 0

    private var scaleGestureDetector: ScaleGestureDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideNavigation(this@PlayerActivity)
        gestureDetectorCompat = GestureDetectorCompat(this@PlayerActivity, this@PlayerActivity)
        scaleGestureDetector = ScaleGestureDetector(
            this@PlayerActivity,
            CustomOnScaleGestureListener(binding.styledPlayerView)
        )
        url = intent.getStringExtra("URL").toString()
        videoQuality = intent.getIntExtra("video", 0)
        audioQuality = intent.getIntExtra("audio", 0)
    }


    @SuppressLint("StaticFieldLeak", "ClickableViewAccessibility")
    private fun initPlayer() = runBlocking {
        try {
            player = ExoPlayer.Builder(this@PlayerActivity).build()
            binding.styledPlayerView.player = player

            object : YouTubeExtractor(this@PlayerActivity) {
                override fun onExtractionComplete(
                    ytFiles: SparseArray<YtFile>?,
                    videoMeta: VideoMeta?
                ) {
                    if (ytFiles.isNotNull()) {

                        val videoUrl = ytFiles?.get(videoQuality)?.url ?: ""
                        val audioUrl = ytFiles?.get(audioQuality)?.url ?: ""
                        val audio = ytFiles?.get(audioQuality)?.url ?: ""

                        val audioSource: MediaSource = ProgressiveMediaSource
                            .Factory(DefaultHttpDataSource.Factory())
                            .createMediaSource(MediaItem.fromUri(audioUrl))

                        val videoSource: MediaSource = ProgressiveMediaSource
                            .Factory(DefaultHttpDataSource.Factory())
                            .createMediaSource(MediaItem.fromUri(videoUrl))

                        player?.setMediaSource(
                            MergingMediaSource(true, videoSource, audioSource),
                            true
                        )

                        player?.prepare()
                        player?.playWhenReady = playWhenReady
                        player?.seekTo(currentWindow, playBackPosition)

                    }
                }
            }.extract(url)

            binding.styledPlayerView.setOnTouchListener { _, motionEvent ->
                gestureDetectorCompat.onTouchEvent(motionEvent)
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    binding.btnBrightness.hide()
                    binding.btnVolume.hide()
                    hideNavigation(this@PlayerActivity)
                }
                return@setOnTouchListener false
            }

        } catch (e: Exception) {
            Log.e("initPlayer", "${e.printStackTrace()}")
        }
    }

    override fun onResume() {
        super.onResume()
        if (audioManager == null) audioManager =
            this@PlayerActivity.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager!!.requestAudioFocus(
            this,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        )
        if (Util.SDK_INT < 24 || player.isNull()) {
            initPlayer()
        }
        if (brightness != 0) setScreenBrightness(brightness)
    }

    override fun onPause() {
        if (Util.SDK_INT < 24) releasePlayer()
        super.onPause()
    }

    override fun onStop() {
        if (Util.SDK_INT < 24) releasePlayer()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.pause()
        audioManager?.abandonAudioFocus(this)
    }

    private fun releasePlayer() {
        if (player.isNotNull()) {
            playWhenReady = player?.playWhenReady ?: false
            playBackPosition = player?.currentPosition ?: 0
            currentWindow = player?.currentWindowIndex ?: 0
            player?.release()
            player = null
        }
    }

    override fun onDown(p0: MotionEvent?): Boolean = false
    override fun onShowPress(p0: MotionEvent?) = Unit
    override fun onSingleTapUp(p0: MotionEvent?): Boolean = false
    override fun onLongPress(p0: MotionEvent?) = Unit
    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean = false

    override fun onScroll(
        event: MotionEvent?,
        event1: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {

        val screenWidth = Resources.getSystem().displayMetrics.widthPixels

        if (abs(distanceX) < abs(distanceY)) {
            if (event!!.x < screenWidth / 2) {
                binding.btnBrightness.show()
                binding.btnVolume.hide()

                val newValue = if (distanceY > 0) brightness + 1 else brightness - 1
                if (newValue in 0..30) brightness = newValue
                binding.btnBrightness.text = brightness.toString()
                setScreenBrightness(brightness)
            } else {
                binding.btnBrightness.hide()
                binding.btnVolume.show()

                val maxVolume = audioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                val newValue = if (distanceY > 0) volume + 1 else volume - 1
                if (newValue in 0..30) volume = newValue
                binding.btnVolume.text = volume.toString()
                audioManager!!.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)

            }
        }

        return true
    }

    private fun setScreenBrightness(value: Int) = runBlocking {
        val d = 1.0f / 30
        val lp = this@PlayerActivity.window?.attributes
        lp?.screenBrightness = d * value
        this@PlayerActivity.window?.attributes = lp
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if (focusChange <= 0) player?.pause()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleGestureDetector?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

}