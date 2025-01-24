package com.clover.sdk.internal.util

import android.app.Application
import android.content.ContentProviderClient
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.os.RemoteException
import android.util.Log
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.MockedStatic
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.`when`
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.FileDescriptor

@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class UnstableContentResolverClientTest {

  private lateinit var mockContext: Context
  private lateinit var mockContentResolver: ContentResolver
  private lateinit var mockContentProviderClient: ContentProviderClient
  private val uri = Uri.parse("content://com.clover.sdk.test")
  private lateinit var unstableContentResolverClient: UnstableContentResolverClient

  @Before
  fun setup() {
    mockContext = mock(Context::class.java)
    mockContentResolver = mock(ContentResolver::class.java)
    `when`(mockContext.contentResolver).thenReturn(mockContentResolver)
    mockContentProviderClient = mock(ContentProviderClient::class.java)
    `when`(mockContentResolver.acquireUnstableContentProviderClient(any<Uri>())).thenReturn(
        mockContentProviderClient
    )

    val uri = Uri.parse("content://com.clover.sdk.test")
    unstableContentResolverClient = UnstableContentResolverClient(mockContentResolver, uri)

    // Speed up tests
    UnstableContentResolverClient.RETRY_DELAY_MS = 50
  }

  private fun assertWithLog(
    expectedLogLevel: Int,
    verify: () -> Unit,
  ) {
    mockStatic(Log::class.java).use { mockedLog ->
      verify()
      verifyLog(mockedLog, expectedLogLevel)
    }
  }

  private fun assertWithoutLog(
    verify: () -> Unit,
  ) {
    mockStatic(Log::class.java).use { mockedLog ->
      verify()
      mockedLog.verify({ Log.v(anyString(), anyString()) }, times(0))
      mockedLog.verify({ Log.d(anyString(), anyString()) }, times(0))
      mockedLog.verify({ Log.i(anyString(), anyString()) }, times(0))
      mockedLog.verify({ Log.w(anyString(), anyString()) }, times(0))
      mockedLog.verify({ Log.e(anyString(), anyString(), any<Exception>()) }, times(0))
    }
  }

  private fun verifyLog(mockedLog: MockedStatic<Log>, level: Int) {
    when (level) {
      Log.INFO -> mockedLog.verify({ Log.i(anyString(), anyString()) }, times(1))
      Log.ERROR -> mockedLog.verify({ Log.e(anyString(), anyString(), any<Exception>()) }, times(1))
      else -> fail("Unsupported log level / invocation")
    }
  }

  private fun mockParcelFileDescriptor(): ParcelFileDescriptor {
    val mockParcelFileDescriptor = mock(ParcelFileDescriptor::class.java)
    val mockFileDescriptor = mock(FileDescriptor::class.java)
    `when`(mockParcelFileDescriptor.fileDescriptor).thenReturn(mockFileDescriptor)
    return mockParcelFileDescriptor
  }

  @Test
  fun testInsert_success() {
    val resultUri = Uri.parse("content://com.clover.sdk.test/1")
    val mockValues = mock(ContentValues::class.java)

    `when`(mockContentProviderClient.insert(uri, mockValues)).thenReturn(resultUri)

    assertWithoutLog {
      val r = unstableContentResolverClient.insert(mockValues)

      verify(mockContentProviderClient, times(1)).insert(uri, mockValues)
      assertEquals(resultUri, r)
    }
  }

  @Test
  fun testUpdate_success() {
    val mockValues = mock(ContentValues::class.java)

    `when`(mockContentProviderClient.update(uri, mockValues, null, null)).thenReturn(1)

    assertWithoutLog {
      val r = unstableContentResolverClient.update(mockValues, null, null)

      verify(mockContentProviderClient, times(1)).update(uri, mockValues, null, null)
      assertEquals(1, r)
    }
  }

  @Test
  fun testDelete_success() {
    `when`(mockContentProviderClient.delete(uri, null, null)).thenReturn(1)

    assertWithoutLog {
      val r = unstableContentResolverClient.delete(null, null)

      verify(mockContentProviderClient, times(1)).delete(uri, null, null)
      assertEquals(1, r)
    }
  }

  @Test
  fun testQuery_success() {
    val mockCursor = mock(Cursor::class.java)

    `when`(mockContentProviderClient
      .query(uri, null, null, null, null))
      .thenReturn(mockCursor)

    assertWithoutLog {
      val r = unstableContentResolverClient.query(null, null, null, null)

      verify(mockContentProviderClient, times(1)).query(uri, null, null, null, null)
      assertEquals(mockCursor, r)
    }
  }

  @Test
  fun testCall_success() {
    val resultBundle = Bundle()

    `when`(mockContentProviderClient.call("M", "A", Bundle.EMPTY)).thenReturn(resultBundle)

    assertWithoutLog {
      val r = unstableContentResolverClient.call("M", "A", Bundle.EMPTY, null)

      verify(mockContentProviderClient, times(1)).call("M", "A", Bundle.EMPTY)
      assertEquals(resultBundle, r)
    }
  }

  @Test
  fun testOpenAssetFile_success() {
    `when`(mockContentProviderClient.openAssetFile(uri, "r"))
      .thenReturn(mock(AssetFileDescriptor::class.java))

    assertWithoutLog {
      val r = unstableContentResolverClient.openAssetFile("r")

      verify(mockContentProviderClient, times(1)).openAssetFile(uri, "r")
      assertNotNull(r)
    }
  }

  @Test
  fun testOpenInputStream_success() {
    val mockParcelFileDescriptor = mockParcelFileDescriptor()

    `when`(mockContentProviderClient.openFile(uri, "r"))
      .thenReturn(mockParcelFileDescriptor)

    assertWithoutLog {
      val r = unstableContentResolverClient.openInputStream("r")

      verify(mockContentProviderClient, times(1)).openFile(uri, "r")
      assertNotNull(r)
    }
  }

  @Test
  fun testInsert_fail_nonRetryable() {
    val mockValues = mock(ContentValues::class.java)

    `when`(
        mockContentProviderClient
          .insert(uri, mockValues)
    )
      .thenThrow(IllegalArgumentException())
      .thenReturn(Uri.parse("content://com.clover.sdk.test/1"))

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.insert(mockValues)

          verify(mockContentProviderClient).insert(uri, mockValues)
          assertNull(r)
        },
    )
  }

  @Test
  fun testUpdate_fail_nonRetryable() {
    val mockValues = mock(ContentValues::class.java)

    `when`(
        mockContentProviderClient
          .update(uri, mockValues, null, null)
    )
      .thenThrow(IllegalArgumentException())
      .thenReturn(1)

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.update(mockValues, null, null)

          verify(mockContentProviderClient, times(1)).update(uri, mockValues, null, null)
          assertEquals(0, r)
        }
    )
  }

  @Test
  fun testDelete_fail_nonRetryable() {
    `when`(
        mockContentProviderClient
          .delete(uri, null, null)
    )
      .thenThrow(IllegalArgumentException())
      .thenReturn(1)

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.delete(null, null)

          verify(mockContentProviderClient, times(1)).delete(uri, null, null)
          assertEquals(0, r)
        }
    )
  }

  @Test
  fun testQuery_fail_nonRetryable() {
    `when`(
        mockContentProviderClient
          .query(uri, null, null, null, null)
    )
      .thenThrow(IllegalArgumentException())
      .thenReturn(mock(Cursor::class.java))

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.query(null, null, null, null)

          verify(mockContentProviderClient, times(1)).query(uri, null, null, null, null)
          assertNull(r)
        }
    )
  }

  @Test
  fun testOpenAssetFile_fail_nonRetryable() {
    `when`(
        mockContentProviderClient
          .openAssetFile(uri, "r")
    )
      .thenThrow(IllegalArgumentException())
      .thenReturn(mock(AssetFileDescriptor::class.java))

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.openAssetFile("r")

          verify(mockContentProviderClient, times(1)).openAssetFile(uri, "r")
          assertNull(r)
        }
    )
  }

  @Test
  fun testOpenInputStream_fail_nonRetryable() {
    `when`(
        mockContentProviderClient
          .openFile(uri, "r")
    )
      .thenThrow(IllegalArgumentException())
      .thenReturn(mock(ParcelFileDescriptor::class.java))

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.openInputStream("r")

          verify(mockContentProviderClient, times(1)).openFile(uri, "r")
          assertNull(r)
        }
    )
  }

  @Test
  fun testInsert_fail_interrupted() {
    val mockValues = mock(ContentValues::class.java)

    `when`(mockContentProviderClient.insert(uri, mockValues))
      .thenReturn(Uri.parse("content://com.clover.sdk.test/1"))

    assertWithLog(
        Log.INFO,
        {
          Thread.currentThread().interrupt()
          val r = unstableContentResolverClient.insert(mockValues)

          verify(mockContentProviderClient, times(0)).insert(uri, mockValues)
          assertTrue(Thread.interrupted())
          assertNull(r)
        }
    )
  }

  @Test
  fun testUpdate_fail_interrupted() {
    val mockValues = mock(ContentValues::class.java)

    `when`(mockContentProviderClient.update(uri, mockValues, null, null))
      .thenReturn(1)

    assertWithLog(
        Log.INFO,
        {
          Thread.currentThread().interrupt()
          val r = unstableContentResolverClient.update(mockValues, null, null)

          verify(mockContentProviderClient, times(0)).update(uri, mockValues, null, null)
          assertTrue(Thread.interrupted())
          assertEquals(0, r)
        }
    )
  }

  @Test
  fun testDelete_fail_interrupted() {
    `when`(mockContentProviderClient.delete(uri, null, null))
      .thenReturn(1)

    assertWithLog(
        Log.INFO,
        {
          Thread.currentThread().interrupt()
          val r = unstableContentResolverClient.delete(null, null)

          verify(mockContentProviderClient, times(0)).delete(uri, null, null)
          assertTrue(Thread.interrupted())
          assertEquals(0, r)
        }
    )
  }

  @Test
  fun testQuery_fail_interrupted() {
    `when`(mockContentProviderClient.query(uri, null, null, null, null))
      .thenReturn(mock(Cursor::class.java))

    assertWithLog(
        Log.INFO,
        {
          Thread.currentThread().interrupt()
          val r = unstableContentResolverClient.query(null, null, null, null)

          verify(mockContentProviderClient, times(0)).query(uri, null, null, null, null)
          assertTrue(Thread.interrupted())
          assertNull(r)
        }
    )
  }

  @Test
  fun testCall_fail_interrupted() {
    `when`(mockContentProviderClient.call("A", "M", Bundle.EMPTY))
      .thenReturn(Bundle.EMPTY)

    assertWithLog(
        Log.INFO,
        {
          Thread.currentThread().interrupt()
          val defaultBundle = Bundle()
          val resultBundle =
              unstableContentResolverClient.call("A", "M", Bundle.EMPTY, defaultBundle)

          verify(mockContentProviderClient, times(0)).call("A", "M", Bundle.EMPTY)
          assertEquals(defaultBundle, resultBundle)
          assertTrue(Thread.interrupted())
        }
    )
  }

  @Test
  fun testOpenAssetFile_fail_interrupted() {
    `when`(mockContentProviderClient.openAssetFile(uri, "r"))
      .thenReturn(mock(AssetFileDescriptor::class.java))

    assertWithLog(
        Log.INFO,
        {
          Thread.currentThread().interrupt()
          val r = unstableContentResolverClient.openAssetFile("r")

          verify(mockContentProviderClient, times(0)).openAssetFile(uri, "r")
          assertTrue(Thread.interrupted())
          assertNull(r)
        }
    )
  }

  @Test
  fun testOpenInputStream_fail_interrupted() {
    `when`(mockContentProviderClient.openFile(uri, "r"))
      .thenReturn(mock(ParcelFileDescriptor::class.java))

    assertWithLog(
        Log.INFO,
        {
          Thread.currentThread().interrupt()
          val r = unstableContentResolverClient.openInputStream("r")

          verify(mockContentProviderClient, times(0)).openFile(uri, "r")
          assertTrue(Thread.interrupted())
          assertNull(r)
        }
    )
  }

  @Test
  fun testInsert_fail_retryable() {
    val mockValues = mock(ContentValues::class.java)
    val returnUri = Uri.parse("content://com.clover.sdk.test/1")

    `when`(
        mockContentProviderClient
          .insert(uri, mockValues)
    )
      .thenThrow(RemoteException())
      .thenReturn(returnUri)


    assertWithoutLog {
      val r = unstableContentResolverClient.insert(mockValues)

      verify(mockContentProviderClient, times(2)).insert(uri, mockValues)
      assertEquals(returnUri, r)
    }
  }

  @Test
  fun testUpdate_fail_retryable() {
    val mockValues = mock(ContentValues::class.java)

    `when`(
        mockContentProviderClient
          .update(uri, mockValues, null, null)
    )
      .thenThrow(RemoteException())
      .thenReturn(1)

    assertWithoutLog {
      val r = unstableContentResolverClient.update(mockValues, null, null)

      verify(mockContentProviderClient, times(2)).update(uri, mockValues, null, null)
      assertEquals(1, r)
    }
  }

  @Test
  fun testDelete_fail_retryable() {
    `when`(
        mockContentProviderClient
          .delete(uri, null, null)
    )
      .thenThrow(RemoteException())
      .thenReturn(1)

    assertWithoutLog {
      val r = unstableContentResolverClient.delete(null, null)

      verify(mockContentProviderClient, times(2)).delete(uri, null, null)
      assertEquals(1, r)
    }
  }

  @Test
  fun testQuery_fail_retryable() {
    val mockCursor = mock(Cursor::class.java)

    `when`(
        mockContentProviderClient
          .query(uri, null, null, null, null)
    )
      .thenThrow(RemoteException())
      .thenReturn(mockCursor)

    assertWithoutLog {
      val c = unstableContentResolverClient.query(null, null, null, null)

      verify(mockContentProviderClient, times(2)).query(uri, null, null, null, null)
      assertEquals(mockCursor, c)
    }
  }

  @Test
  fun testCall_fail_retryable() {
    val resultBundle = Bundle()

    `when`(
        mockContentProviderClient
          .call("A", "M", Bundle.EMPTY)
    )
      .thenThrow(RemoteException())
      .thenReturn(resultBundle)

    assertWithoutLog {
      val r =
          unstableContentResolverClient.call("A", "M", Bundle.EMPTY, Bundle.EMPTY)

      verify(mockContentProviderClient, times(2)).call("A", "M", Bundle.EMPTY)
      assertEquals(resultBundle, r)
    }
  }

  @Test
  fun testOpenAssetFile_fail_retryable() {
    val mockAssetFileDescriptor = mock(AssetFileDescriptor::class.java)
    `when`(
        mockContentProviderClient
          .openAssetFile(uri, "r")
    )
      .thenThrow(RemoteException())
      .thenReturn(mockAssetFileDescriptor)

    assertWithoutLog {
      val r = unstableContentResolverClient.openAssetFile("r")

      verify(mockContentProviderClient, times(2)).openAssetFile(uri, "r")
      assertEquals(mockAssetFileDescriptor, r)
    }
  }

  @Test
  fun testOpenInputStream_fail_retryable() {
    val mockParcelFileDescriptor = mockParcelFileDescriptor()

    `when`(
        mockContentProviderClient
          .openFile(uri, "r")
    )
      .thenThrow(RemoteException())
      .thenReturn(mockParcelFileDescriptor)

    assertWithoutLog {
      val r = unstableContentResolverClient.openInputStream("r")

      verify(mockContentProviderClient, times(2)).openFile(uri, "r")
      assertNotNull(r)
    }
  }

  @Test
  fun testInsert_fail_retryable_untilFatal() {
    val uri = Uri.parse("content://com.clover.sdk.test")
    val mockValues = mock(ContentValues::class.java)

    `when`(
        mockContentProviderClient
          .insert(uri, mockValues)
    )
      .thenThrow(RemoteException())

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.insert(mockValues)

          verify(mockContentProviderClient, times(UnstableContentResolverClient.MAX_RETRY_ATTEMPTS))
            .insert(uri, mockValues)
          assertNull(r)
        }
    )
  }

  @Test
  fun testUpdate_fail_retryable_untilFatal() {
    val mockValues = mock(ContentValues::class.java)

    `when`(
        mockContentProviderClient
          .update(uri, mockValues, null, null)
    )
      .thenThrow(RemoteException())

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.update(mockValues, null, null)

          verify(mockContentProviderClient, times(UnstableContentResolverClient.MAX_RETRY_ATTEMPTS))
            .update(uri, mockValues, null, null)
          assertEquals(0, r)
        }
    )
  }

  @Test
  fun testDelete_fail_retryable_untilFatal() {
    `when`(
        mockContentProviderClient
          .delete(uri, null, null)
    )
      .thenThrow(RemoteException())

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.delete(null, null)

          verify(mockContentProviderClient, times(UnstableContentResolverClient.MAX_RETRY_ATTEMPTS))
            .delete(uri, null, null)
          assertEquals(0, r)
        }
    )
  }

  @Test
  fun testQuery_fail_retryable_untilFatal() {
    `when`(
        mockContentProviderClient
          .query(uri, null, null, null, null)
    )
      .thenThrow(RemoteException())

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.query(null, null, null, null)

          verify(mockContentProviderClient, times(UnstableContentResolverClient.MAX_RETRY_ATTEMPTS))
            .query(uri, null, null, null, null)
          assertNull(r)
        }
    )
  }

  @Test
  fun testCall_fail_retryable_untilFatal() {
    `when`(
        mockContentProviderClient
          .call("A", "M", Bundle.EMPTY)
    )
      .thenThrow(RemoteException())

    assertWithLog(
        Log.ERROR,
        {
          val defaultBundle = Bundle()
          val resultBundle =
              unstableContentResolverClient.call("A", "M", Bundle.EMPTY, defaultBundle)

          verify(mockContentProviderClient, times(UnstableContentResolverClient.MAX_RETRY_ATTEMPTS))
            .call("A", "M", Bundle.EMPTY)
          assertEquals(defaultBundle, resultBundle)
        }
    )
  }

  @Test
  fun testOpenAssetFile_fail_retryable_untilFatal() {
    `when`(
        mockContentProviderClient
          .openAssetFile(uri, "r")
    )
      .thenThrow(RemoteException())

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.openAssetFile("r")

          verify(mockContentProviderClient, times(UnstableContentResolverClient.MAX_RETRY_ATTEMPTS))
            .openAssetFile(uri, "r")
          assertNull(r)
        }
    )
  }

  @Test
  fun testOpenInputStream_fail_retryable_untilFatal() {
    `when`(
        mockContentProviderClient
          .openFile(uri, "r")
    )
      .thenThrow(RemoteException())

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.openInputStream("r")

          verify(mockContentProviderClient, times(UnstableContentResolverClient.MAX_RETRY_ATTEMPTS))
            .openFile(uri, "r")
          assertNull(r)
        }
    )
  }

  @Test
  fun testCall_fail_retryable_noDefault() {
    val remoteException = RemoteException()
    `when`(
        mockContentProviderClient
          .call("A", "M", Bundle.EMPTY)
    )
      .thenThrow(remoteException)

    unstableContentResolverClient.noDefault = true
    assertWithoutLog {
      try {
        unstableContentResolverClient.call("A", "M", Bundle.EMPTY, Bundle.EMPTY)
        fail("Expected RemoteException")
      } catch (rte: RuntimeException) {
        assertEquals(remoteException, rte.cause)
        verify(mockContentProviderClient, times(UnstableContentResolverClient.MAX_RETRY_ATTEMPTS))
          .call("A", "M", Bundle.EMPTY)
      }
    }
  }

  @Test
  fun testInsert_fail_UnsupportedOperationException_quiet() {
    val mockContentValues = mock(ContentValues::class.java)
    val exception = UnsupportedOperationException()
    `when`(mockContentProviderClient.insert(uri, mockContentValues))
      .thenThrow(exception)

    unstableContentResolverClient.quiet = true
    assertWithoutLog {
      val r = unstableContentResolverClient.insert(mockContentValues)

      verify(mockContentProviderClient, times(1)).insert(uri, mockContentValues)
      assertNull(r)
    }
  }

  @Test
  fun testUpdate_fail_UnsupportedOperationException_quiet() {
    val mockContentValues = mock(ContentValues::class.java)
    val exception = UnsupportedOperationException()
    `when`(mockContentProviderClient.update(uri, mockContentValues, null, null))
      .thenThrow(exception)

    unstableContentResolverClient.quiet = true
    assertWithoutLog {
      val r = unstableContentResolverClient.update(mockContentValues, null, null)

      verify(mockContentProviderClient, times(1)).update(uri, mockContentValues, null, null)
      assertEquals(0, r)
    }
  }

  @Test
  fun testDelete_fail_UnsupportedOperationException_quiet() {
    val exception = UnsupportedOperationException()
    `when`(mockContentProviderClient.delete(uri, null, null))
      .thenThrow(exception)

    unstableContentResolverClient.quiet = true
    assertWithoutLog {
      val r = unstableContentResolverClient.delete(null, null)

      verify(mockContentProviderClient, times(1)).delete(uri, null, null)
      assertEquals(0, r)
    }
  }

  @Test
  fun testQuery_fail_UnsupportedOperationException_quiet() {
    val exception = UnsupportedOperationException()
    `when`(mockContentProviderClient.query(uri, null, null, null, null))
      .thenThrow(exception)

    unstableContentResolverClient.quiet = true
    assertWithoutLog {
      val r = unstableContentResolverClient.query(null, null, null, null)

      verify(mockContentProviderClient, times(1)).query(uri, null, null, null, null)
      assertNull(r)
    }
  }

  @Test
  fun testCall_fail_UnsupportedOperationException_quiet() {
    val exception = UnsupportedOperationException()
    `when`(
        mockContentProviderClient
          .call("A", "M", Bundle.EMPTY)
    )
      .thenThrow(exception)

    unstableContentResolverClient.quiet = true
    assertWithoutLog {
      val defaultBundle = Bundle()
      val result = unstableContentResolverClient.call("A", "M", Bundle.EMPTY, defaultBundle)
      verify(mockContentProviderClient, times(1)).call("A", "M", Bundle.EMPTY)
      assertEquals(defaultBundle, result)
    }
  }

  @Test
  fun testOpenAssetFile_fail_UnsupportedOperationException_quiet() {
    val exception = UnsupportedOperationException()
    `when`(mockContentProviderClient.openAssetFile(uri, "r"))
      .thenThrow(exception)

    unstableContentResolverClient.quiet = true
    assertWithoutLog {
      val r = unstableContentResolverClient.openAssetFile("r")

      verify(mockContentProviderClient, times(1)).openAssetFile(uri, "r")
      assertNull(r)
    }
  }

  @Test
  fun testOpenInputStream_fail_UnsupportedOperationException_quiet() {
    val exception = UnsupportedOperationException()

    `when`(mockContentProviderClient.openFile(uri, "r"))
      .thenThrow(exception)
    unstableContentResolverClient.quiet = true

    assertWithoutLog {
      val r = unstableContentResolverClient.openInputStream("r")

      verify(mockContentProviderClient, times(1)).openFile(uri, "r")
      assertNull(r)
    }
  }

  @Test
  fun testInsert_fail_UnsupportedOperationException_notQuiet() {
    val mockContentValues = mock(ContentValues::class.java)
    val exception = UnsupportedOperationException()

    `when`(mockContentProviderClient.insert(uri, mockContentValues))
      .thenThrow(exception)
    unstableContentResolverClient.quiet = false

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.insert(mockContentValues)

          verify(mockContentProviderClient, times(1)).insert(uri, mockContentValues)
          assertNull(r)
        },
    )
  }

  @Test
  fun testUpdate_fail_UnsupportedOperationException_notQuiet() {
    val mockContentValues = mock(ContentValues::class.java)
    val exception = UnsupportedOperationException()

    `when`(mockContentProviderClient.update(uri, mockContentValues, null, null))
      .thenThrow(exception)
    unstableContentResolverClient.quiet = false

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.update(mockContentValues, null, null)

          verify(mockContentProviderClient, times(1)).update(uri, mockContentValues, null, null)
          assertEquals(0, r)
        },
    )
  }

  @Test
  fun testDelete_fail_UnsupportedOperationException_notQuiet() {
    val exception = UnsupportedOperationException()

    `when`(mockContentProviderClient.delete(uri, null, null))
      .thenThrow(exception)
    unstableContentResolverClient.quiet = false

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.delete(null, null)

          verify(mockContentProviderClient, times(1)).delete(uri, null, null)
          assertEquals(0, r)
        },
    )
  }

  @Test
  fun testQuery_fail_UnsupportedOperationException_notQuiet() {
    val exception = UnsupportedOperationException()

    `when`(mockContentProviderClient.query(uri, null, null, null, null))
      .thenThrow(exception)
    unstableContentResolverClient.quiet = false

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.query(null, null, null, null)

          verify(mockContentProviderClient, times(1)).query(uri, null, null, null, null)
          assertNull(r)
        },
    )
  }

  @Test
  fun testOpenAssetFile_fail_UnsupportedOperationException_notQuiet() {
    val exception = UnsupportedOperationException()

    `when`(mockContentProviderClient.openAssetFile(uri, "r"))
      .thenThrow(exception)
    unstableContentResolverClient.quiet = false

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.openAssetFile("r")

          verify(mockContentProviderClient, times(1)).openAssetFile(uri, "r")
          assertNull(r)
        },
    )
  }

  @Test
  fun testOpenInputStream_fail_UnsupportedOperationException_notQuiet() {
    val exception = UnsupportedOperationException()

    `when`(mockContentProviderClient.openFile(uri, "r"))
      .thenThrow(exception)
    unstableContentResolverClient.quiet = false

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.openInputStream("r")

          verify(mockContentProviderClient, times(1)).openFile(uri, "r")
          assertNull(r)
        },
    )
  }

  @Test
  fun testCall_fail_UnsupportedOperationException_notQuiet() {
    val exception = UnsupportedOperationException()
    val defaultBundle = Bundle()

    `when`(
        mockContentProviderClient
          .call("A", "M", Bundle.EMPTY)
    )
      .thenThrow(exception)
    unstableContentResolverClient.quiet = false

    assertWithLog(
        Log.ERROR,
        {
          val r = unstableContentResolverClient.call("A", "M", Bundle.EMPTY, defaultBundle)

          verify(mockContentProviderClient, times(1)).call("A", "M", Bundle.EMPTY)
          assertEquals(defaultBundle, r)
        },
    )
  }

  @Test
  fun testCall_fail_UnsupportedOperationException_notQuiet_noDefault() {
    val exception = UnsupportedOperationException()

    `when`(
        mockContentProviderClient
          .call("A", "M", Bundle.EMPTY)
    )
      .thenThrow(exception)
    unstableContentResolverClient.quiet = false
    unstableContentResolverClient.noDefault = true

    assertWithoutLog {
      try {
        unstableContentResolverClient.call("A", "M", Bundle.EMPTY, Bundle.EMPTY)
        fail("Expected RuntimeException")
      } catch (rte: RuntimeException) {
        verify(mockContentProviderClient, times(1)).call("A", "M", Bundle.EMPTY)
        assertEquals(exception, rte.cause)
      }
    }
  }
}