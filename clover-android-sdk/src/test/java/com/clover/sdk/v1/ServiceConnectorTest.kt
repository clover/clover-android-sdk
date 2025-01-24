package com.clover.sdk.v1

import android.accounts.Account
import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.os.IBinder
import androidx.test.core.app.ApplicationProvider
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v3.employees.IEmployeeService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.any
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.nullable
import org.mockito.Mockito.spy
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.Executor

@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class ServiceConnectorTest {

  private val context = ApplicationProvider.getApplicationContext<Application>()
  private val account = Account("test_account", CloverAccount.CLOVER_ACCOUNT_TYPE)

  class TestServiceConnector(
    context: Context?,
    account: Account?,
    client: OnServiceConnectedListener?,
    serviceExecutor: Executor
  ) : ServiceConnector<IEmployeeService>(context, account, client, serviceExecutor) {

    val mockServiceInterface = mock(IEmployeeService::class.java)

    override fun getServiceIntentAction() = "com.clover.printers.TestService"
    override fun getServiceInterface(iBinder: IBinder?) = mockServiceInterface
  }

  class SynchronousExecutor: Executor {
    override fun execute(command: Runnable?) {
      command?.run()
    }
  }

  /**
   * Verify that if the service executor's thread is interrupted, the call will throw a
   * [BindingException].
   */
  @Test
  fun testWaitForConnection_interrupted() {
    val connector = TestServiceConnector(context, account, null, SynchronousExecutor())
    val spyConnector = spy(connector)
    doThrow(InterruptedException()).`when`(spyConnector).doWait(anyLong())

    try {
      spyConnector.waitForConnection()
    } catch (e: BindingException) {
      // Success
      Assert.assertTrue(e.cause is InterruptedException)
    }
  }
}
