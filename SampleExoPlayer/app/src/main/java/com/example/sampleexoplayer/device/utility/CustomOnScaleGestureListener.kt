package com.example.sampleexoplayer.device.utility

import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView

class CustomOnScaleGestureListener(
    private val player: StyledPlayerView
) : SimpleOnScaleGestureListener() {
    private var scaleFactor = 0f

    override fun onScale(
        detector: ScaleGestureDetector
    ): Boolean {
        scaleFactor = detector.scaleFactor
        return true
    }

    override fun onScaleBegin(
        detector: ScaleGestureDetector
    ): Boolean {
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {
        if (scaleFactor > 1) {
            player.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        } else {
            player.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        }
    }
}
