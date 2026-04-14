package com.example.photogalleryapp

import android.graphics.Matrix
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewTreeObserver
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class FullscreenActivity : AppCompatActivity() {

    val matrix = Matrix()
    var scaleFactor = 1f
    var lastDistance = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        val resourceId = intent.getIntExtra("resourceId", 0)
        val imgFullscreen = findViewById<ImageView>(R.id.imgFullscreen)
        imgFullscreen.setImageResource(resourceId)

        imgFullscreen.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                imgFullscreen.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val w = imgFullscreen.width.toFloat()
                val h = imgFullscreen.height.toFloat()
                matrix.setScale(1f, 1f, w / 2f, h / 2f)
                imgFullscreen.imageMatrix = matrix
            }
        })

        imgFullscreen.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_POINTER_DOWN -> {
                    lastDistance = getDistance(event)
                }
                MotionEvent.ACTION_MOVE -> {
                    if (event.pointerCount == 2) {
                        val newDistance = getDistance(event)
                        if (lastDistance > 0f) {
                            val scale = newDistance / lastDistance
                            scaleFactor *= scale
                            scaleFactor = scaleFactor.coerceIn(0.5f, 5f)
                            val cx = (event.getX(0) + event.getX(1)) / 2f
                            val cy = (event.getY(0) + event.getY(1)) / 2f
                            matrix.setScale(scaleFactor, scaleFactor, cx, cy)
                            imgFullscreen.imageMatrix = matrix
                        }
                        lastDistance = newDistance
                    }
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    lastDistance = 0f
                }
            }
            true
        }

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }
    }

    fun getDistance(event: MotionEvent): Float {
        val dx = event.getX(0) - event.getX(1)
        val dy = event.getY(0) - event.getY(1)
        return sqrt(dx * dx + dy * dy)
    }
}
