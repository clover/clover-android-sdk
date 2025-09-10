package com.clover.android.sdk.examples

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.service.dreams.DreamService
import android.view.View
import com.clover.android.sdk.examples.ScreensaverTestActivity.ScreensaverTestViewModel
import com.clover.sdk.util.Screensaver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.LinkedList
import kotlin.random.Random
import kotlin.time.Duration.Companion.minutes
import kotlin.math.cos
import kotlin.math.sin

class QixDreamService : DreamService() {
  private lateinit var qixView: QixView
  private lateinit var screensaver: Screensaver
  private lateinit var prefs: SharedPreferences

  private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
  private var sleepJob: Job? = null

  override fun onCreate() {
    super.onCreate()
    screensaver = Screensaver(this)
    qixView = QixView(this)

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

    setContentView(qixView)

    sleepJob = serviceScope.launch {
      delay(
          prefs.getInt(
              ScreensaverTestViewModel.PREF_DREAM_BRIGHT_MINUTES,
              ScreensaverTestViewModel.DEFAULT_DREAM_BRIGHT_MINUTES
          ).minutes
      )
      screenBrightness = 0.1f
      delay(
          prefs.getInt(
              ScreensaverTestViewModel.PREF_DREAM_DIM_MINUTES,
              ScreensaverTestViewModel.DEFAULT_DREAM_DIM_MINUTES
          ).minutes
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

  private class Point(
    var x: Float,
    var y: Float,
    speed: Float,
    angle: Float,
    private val width: Int,
    private val height: Int,
  ) {
    var vx: Float = 0f
      private set
    var vy: Float = 0f
      private set

    var speed: Float = speed
      set(value) {
        field = value
        recalculate()
      }

    var angle: Float = angle
      set(value) {
        field = value
        recalculate()
      }

    init {
      recalculate()
    }

    /**
     * This private function is the only place where vx and vy are calculated.
     * It's called whenever a property they depend on (speed or angle) is changed.
     */
    private fun recalculate() {
      vx = cos(angle) * speed
      vy = sin(angle) * speed
    }

    fun step() {
      // 1. Update position based on speed and angle
      x += vx
      y += vy

      // 2. Handle wall collisions by reflecting the angle

      if (x <= 0 || x >= width) {
        // Reflect the angle for a vertical wall collision
        angle = Math.PI.toFloat() - angle
        // Clamp position to prevent getting stuck
        x = x.coerceIn(0f, width.toFloat())
        // Add randomness after the bounce, max of .2 radians
        angle += (random.nextFloat() - 0.5f) * bounceRandomness
      }

      if (y <= 0 || y >= height) {
        // Reflect the angle for a horizontal wall collision
        angle = -angle
        // Clamp position to prevent getting stuck
        y = y.coerceIn(0f, height.toFloat())
        // Add randomness after the bounce, max of .2 radians
        angle += (random.nextFloat() - 0.5f) * bounceRandomness
      }
    }

    fun copy() = Point(x, y, speed, angle, width, height)

    companion object {
      private val random = Random
      private const val bounceRandomness = 0.4f // Radians

      fun createRandom(width: Int, height: Int, speed: Float) = Point(
          x = random.nextInt(width).toFloat(),
          y = random.nextInt(height).toFloat(),
          speed = speed,
          angle = random.nextDouble(0.0, 2 * Math.PI).toFloat(),
          width = width,
          height = height,
      )
    }
  }

  private inner class QixView(context: Context) : View(context) {

    private val trailSize = 3
    private val trailLength = 64
    private val paints = List(trailSize) {
      Paint().apply {
        color = when (it) {
          0 -> Color.GREEN
          1 -> Color.CYAN
          else -> Color.MAGENTA
        }
        alpha = 0xff
        strokeWidth = 2f
        isAntiAlias = true
      }
    }

    private lateinit var lines: Array<Pair<Point, Point>>
    private val trails = Array(trailSize) { LinkedList<Pair<Point, Point>>() }

    // Physics constants
    private var lastUpdateTime = 0L
    private val updateInterval = 64L
    private val speed = 8f

    private lateinit var dreamDebugDisplay: DreamDebugDisplay

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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
      super.onSizeChanged(w, h, oldw, oldh)
      // Initialize lines with points that store speed and angle
      lines = Array(trailSize) {
        Pair(Point.createRandom(w, h, speed), Point.createRandom(w, h, speed))
      }
      for (i in trails.indices) {
        trails[i].clear()
      }
    }

    override fun onDraw(canvas: Canvas) {
      super.onDraw(canvas)
      canvas.drawColor(Color.BLACK)

      if (System.currentTimeMillis() - lastUpdateTime > updateInterval) {
        lastUpdateTime = System.currentTimeMillis()

        for (i in lines.indices) {
          lines[i].first.step()
          lines[i].second.step()
          trails[i].add(Pair(lines[i].first.copy(), lines[i].second.copy()))
        }

        for (trail in trails) {
          if (trail.size > trailLength) {
            trail.removeFirst()
          }
        }
      }

      trails.forEachIndexed { trailIndex, trail ->
        val currentPaint = paints[trailIndex]
        trail.forEachIndexed { segmentIndex, segment ->
          val alpha = (0xff * (segmentIndex + 1)) / trail.size
          currentPaint.alpha = alpha
          canvas.drawLine(
              segment.first.x,
              segment.first.y,
              segment.second.x,
              segment.second.y,
              currentPaint
          )
        }

        dreamDebugDisplay.draw(canvas)
      }

      postInvalidateOnAnimation()
    }
  }
}