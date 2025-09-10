package com.clover.android.sdk.examples

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.service.dreams.DreamService
import android.view.View
import com.clover.sdk.util.Screensaver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import androidx.core.graphics.scale
import com.clover.android.sdk.examples.ScreensaverTestActivity.*
import kotlin.time.Duration.Companion.minutes

class ToasterDreamService : DreamService() {
    private lateinit var toasterView: ToasterView
    private lateinit var screensaver: Screensaver
    private lateinit var prefs: SharedPreferences

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var sleepJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        screensaver = Screensaver(this)
        toasterView = ToasterView(this)

        prefs = getSharedPreferences(ScreensaverTestViewModel.PREFS_SCREENSAVER, MODE_PRIVATE)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        isFullscreen = true
        isInteractive = false

        systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN)

        setContentView(toasterView)

        sleepJob = serviceScope.launch {
            delay(prefs.getInt(
                ScreensaverTestViewModel.PREF_DREAM_BRIGHT_MINUTES,
                ScreensaverTestViewModel.DEFAULT_DREAM_BRIGHT_MINUTES).minutes
            )
            screenBrightness = 0.1f
            delay(prefs.getInt(
                ScreensaverTestViewModel.PREF_DREAM_DIM_MINUTES,
                ScreensaverTestViewModel.DEFAULT_DREAM_DIM_MINUTES).minutes
            )
            screensaver.gotoSleep()
            finish()
        }
    }

    private var systemUiVisibility: Int
        get() = window.decorView.systemUiVisibility
        set(value) {
            window.decorView.systemUiVisibility = value
        }

    private var screenBrightness: Float
        get() = window.attributes.screenBrightness
        set(value) {
            val params = window.attributes
            params.screenBrightness = value
            window.attributes = params

            window.windowManager.updateViewLayout(window.decorView, params);
        }

    override fun onDetachedFromWindow() {
        sleepJob?.cancel().also {
            sleepJob = null
        }
        super.onDetachedFromWindow()
    }

    private inner class ToasterView(context: android.content.Context) : View(context) {
        private val toasters = mutableListOf<Toaster>()
        private val paint = Paint()
        private var toasterBitmap: Bitmap? = null
        private val random = Random
        private val toasterSizeFraction = 0.2f
        private val diagonalSpeed = 5f  // Base speed for diagonal movement
        private lateinit var dreamDebugDisplay: DreamDebugDisplay


        init {
            loadScaledToasterBitmap()
        }

        private fun loadScaledToasterBitmap() {
            val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.toaster)
            val displayMetrics = context.resources.displayMetrics
            val screenHeight = displayMetrics.heightPixels
            val targetHeight = (screenHeight * toasterSizeFraction).toInt()
            val scale = targetHeight.toFloat() / originalBitmap.height
            val targetWidth = (originalBitmap.width * scale).toInt()

            toasterBitmap = originalBitmap.scale(targetWidth, targetHeight)

            if (originalBitmap != toasterBitmap) {
                originalBitmap.recycle()
            }
        }

        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            super.onSizeChanged(w, h, oldw, oldh)
            toasters.clear()
            loadScaledToasterBitmap()

            if (w > 0 && h > 0) {
                repeat(10) { addToaster() }
            }
        }

        override fun onAttachedToWindow() {
            super.onAttachedToWindow()
            dreamDebugDisplay = DreamDebugDisplay(
                brightMinutes = prefs.getInt(
                    ScreensaverTestViewModel.PREF_DREAM_BRIGHT_MINUTES,
                    ScreensaverTestViewModel.DEFAULT_DREAM_BRIGHT_MINUTES
                ),
                dimMinutes = prefs.getInt(
                    ScreensaverTestViewModel.PREF_DREAM_DIM_MINUTES,
                    ScreensaverTestViewModel.DEFAULT_DREAM_DIM_MINUTES
                )
            )
        }

        private fun addToaster() {
            toasterBitmap?.let { bitmap ->
                // Start positions along the top and right edges
                val startOnTop = random.nextBoolean()
                val x: Float
                val y: Float

                if (startOnTop) {
                    // Start somewhere along the top edge, right half of screen
                    x = (width / 2 + random.nextInt(width / 2)).toFloat()
                    y = -bitmap.height.toFloat()
                } else {
                    // Start somewhere along the right edge, top half of screen
                    x = width.toFloat()
                    y = random.nextInt(height / 2).toFloat()
                }

                // Calculate speed variation (80% to 120% of base speed)
                val speedVariation = 0.8f + random.nextFloat() * 0.4f
                val speed = diagonalSpeed * speedVariation

                toasters.add(Toaster(x, y, speed, bitmap))
            }
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvas.drawColor(Color.BLACK)

            toasters.forEach { toaster ->
                toaster.update()
                toaster.draw(canvas, paint)

                // Reset toaster position when it goes off screen (left or bottom)
                if (toaster.x < -toaster.bitmap.width || toaster.y > height) {
                    // Randomly choose whether to reset to top or right edge
                    if (random.nextBoolean()) {
                        toaster.x = (width / 2 + random.nextInt(width / 2)).toFloat()
                        toaster.y = -toaster.bitmap.height.toFloat()
                    } else {
                        toaster.x = width.toFloat()
                        toaster.y = random.nextInt(height / 2).toFloat()
                    }
                }
            }

            dreamDebugDisplay.draw(canvas)

            postInvalidateOnAnimation()
        }

        private inner class Toaster(
            var x: Float,
            var y: Float,
            private val speed: Float,
            val bitmap: Bitmap
        ) {
            private val matrix = Matrix()
            private var rotation = 0f
            private val rotationSpeed = random.nextFloat() * 4f - 2f // -2 to 2 degrees per frame
            private val baseAngle = 225f // 225 degrees = moving down and left

            fun update() {
                // Move diagonally (down and left)
                x -= speed  // Negative for left movement
                y += speed  // Positive for downward movement

                // Update rotation for tumbling effect
                rotation += rotationSpeed
                if (rotation > 360f) rotation -= 360f
                else if (rotation < 0f) rotation += 360f
            }

            fun draw(canvas: Canvas, paint: Paint) {
                matrix.reset()
                matrix.postTranslate(-bitmap.width / 2f, -bitmap.height / 2f)
                matrix.postRotate(rotation + baseAngle)  // Add base angle to tumble rotation
                matrix.postTranslate(x + bitmap.width / 2f, y + bitmap.height / 2f)
                canvas.drawBitmap(bitmap, matrix, paint)
            }
        }
    }
}