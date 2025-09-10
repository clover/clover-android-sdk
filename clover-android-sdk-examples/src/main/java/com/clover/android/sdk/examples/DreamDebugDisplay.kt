package com.clover.android.sdk.examples

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.SystemClock
import java.util.Locale

/**
 * Draws a small window on a canvas that displays elapsed time. Use this for timing information
 * for dreams.
 */
class DreamDebugDisplay(
  private val brightMinutes: Int,
  private val dimMinutes: Int,
) {
  private val textPaint = Paint().apply {
    color = Color.WHITE
    textSize = 24f
    typeface = Typeface.MONOSPACE
    isAntiAlias = true
    alpha = 200 // Slightly transparent so it's less intrusive
  }

  private val strokePaint = Paint().apply {
    color = Color.BLACK
    textSize = 24f
    typeface = Typeface.MONOSPACE
    isAntiAlias = true
    alpha = 200
    style = Paint.Style.STROKE
    strokeWidth = 3f
  }

  private val backgroundPaint = Paint().apply {
    color = Color.BLACK
    alpha = 128
  }

  private var startTime = SystemClock.elapsedRealtime()
  private val textBounds = Rect()

  fun draw(canvas: Canvas) {
    val currentTime = SystemClock.elapsedRealtime()
    val elapsedSeconds = (currentTime - startTime) / 1000

    val hours = elapsedSeconds / 3600
    val minutes = (elapsedSeconds % 3600) / 60
    val seconds = elapsedSeconds % 60

    val timeText = String.format(Locale.ROOT, "%02d:%02d:%02d", hours, minutes, seconds)

    // Format bright and dim times as hh:mm:ss
    val brightSeconds = brightMinutes * 60L
    val brightHours = brightSeconds / 3600
    val brightMins = (brightSeconds % 3600) / 60
    val brightSecs = brightSeconds % 60
    val brightTimeText = String.format(Locale.ROOT, "%02d:%02d:%02d", brightHours, brightMins, brightSecs)

    val dimSecondsTotal = dimMinutes * 60L
    val dimHours = dimSecondsTotal / 3600
    val dimMins = (dimSecondsTotal % 3600) / 60
    val dimSecs = dimSecondsTotal % 60
    val dimTimeText = String.format(Locale.ROOT, "%02d:%02d:%02d", dimHours, dimMins, dimSecs)

    val debugText = """
            |Elapsed: $timeText
            |Bright:  $brightTimeText
            |Dim:     $dimTimeText
        """.trimMargin()

    // Calculate text dimensions
    textPaint.getTextBounds(debugText, 0, debugText.length, textBounds)

    val padding = 16f
    val lineHeight = textPaint.textSize + 4f
    val lines = debugText.split('\n')

    // Calculate position (bottom-right corner)
    val textWidth = lines.maxOfOrNull { line ->
      textPaint.measureText(line)
    } ?: 0f

    val textHeight = lines.size * lineHeight
    val x = canvas.width - textWidth - padding
    val y = canvas.height - textHeight - padding

    // Draw background
    canvas.drawRect(
        x - padding / 2,
        y - padding / 2,
        x + textWidth + padding / 2,
        y + textHeight + padding / 2,
        backgroundPaint
    )

    // Draw text lines with stroke first, then fill
    lines.forEachIndexed { index, line ->
      val textY = y + (index + 1) * lineHeight
      // Draw stroke first
      canvas.drawText(line, x, textY, strokePaint)
      // Draw fill on top
      canvas.drawText(line, x, textY, textPaint)
    }
  }
}