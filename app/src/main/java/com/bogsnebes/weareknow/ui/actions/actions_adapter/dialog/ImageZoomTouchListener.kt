package com.bogsnebes.weareknow.ui.actions.actions_adapter.dialog

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView

class ImageZoomTouchListener(context: Context) :
    ScaleGestureDetector.SimpleOnScaleGestureListener(), View.OnTouchListener {

    private val scaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(context, this)
    private var scaleFactor = 1.0f

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        if (v is ImageView) {
            v.scaleX = scaleFactor
            v.scaleY = scaleFactor
        }
        return true
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        scaleFactor *= detector.scaleFactor
        scaleFactor = scaleFactor.coerceIn(MIN_SCALE_FACTOR, MAX_SCALE_FACTOR)
        return true
    }

    companion object {
        private const val MIN_SCALE_FACTOR = 1.0f
        private const val MAX_SCALE_FACTOR = 3.0f
    }
}