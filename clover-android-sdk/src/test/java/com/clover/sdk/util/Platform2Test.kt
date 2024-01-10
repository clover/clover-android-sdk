package com.clover.sdk.util

import android.content.Context
import android.graphics.Point
import android.view.Display
import android.view.WindowManager
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.stubbing.Answer
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class Platform2Test {
  private lateinit var mockContext: Context
  private lateinit var mockWindowManager: WindowManager
  private lateinit var mockDisplay: Display

  @Before
  fun setup() {
    mockDisplay = mock(Display::class.java)

    mockWindowManager = mock(WindowManager::class.java)
    `when`(mockWindowManager.defaultDisplay).thenReturn(mockDisplay)

    mockContext = mock(Context::class.java)
    `when`(mockContext.getSystemService(Context.WINDOW_SERVICE)).thenReturn(mockWindowManager)
  }

  @Test
  fun testSquareDisplay() {
    doAnswer(Answer { invocation ->
      val point = invocation.getArgument<Point>(0)
      point.x = 480
      point.y = 480
      null
    }).`when`(mockDisplay).getSize(any(Point::class.java))

    Assert.assertTrue(Platform2.isSquareDisplay(mockContext))
  }

  @Test
  fun testAlmostSquareDisplay() {
    doAnswer(Answer { invocation ->
      val point = invocation.getArgument<Point>(0)
      point.x = 480
      point.y = 600
      null
    }).`when`(mockDisplay).getSize(any(Point::class.java))

    Assert.assertTrue(Platform2.isSquareDisplay(mockContext))
  }

  @Test
  fun testPortDisplay() {
    doAnswer(Answer { invocation ->
      val point = invocation.getArgument<Point>(0)
      point.x = 720
      point.y = 1280
      null
    }).`when`(mockDisplay).getSize(any(Point::class.java))

    Assert.assertFalse(Platform2.isSquareDisplay(mockContext))
  }

  @Test
  fun testLandDisplay() {
    doAnswer(Answer { invocation ->
      val point = invocation.getArgument<Point>(0)
      point.x = 1920
      point.y = 1080
      null
    }).`when`(mockDisplay).getSize(any(Point::class.java))

    Assert.assertFalse(Platform2.isSquareDisplay(mockContext))
  }
}