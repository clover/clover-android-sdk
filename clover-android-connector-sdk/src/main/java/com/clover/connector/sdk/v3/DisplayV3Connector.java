/**
 * Copyright (C) 2016 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.connector.sdk.v3;

import com.clover.sdk.FdParcelable;
import com.clover.sdk.v1.ServiceConnector;

import android.accounts.Account;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Service connector for {@link IDisplayServiceV3}. Please see that class for documentation on the
 * RPC methods.
 *
 * @see IDisplayServiceV3
 * @see ServiceConnector
 */
public class DisplayV3Connector extends ServiceConnector<IDisplayServiceV3> {

  // TODO: MRH: Not sure about this right now...this is payments app
  private static final String SERVICE_HOST = "com.clover.payment.builder.pay";

  private final List<WeakReference<DisplayServiceListener>> mDisplayServiceListener = new CopyOnWriteArrayList<WeakReference<DisplayServiceListener>>();
  private DisplayServiceListenerParent mListener;

  /**
   * Constructs a new DisplayConnector object.
   *
   * @param context the Context object, required for establishing a connection to
   *                the service.
   * @param account the Account to use with the service.
   * @param client  an optional object implementing the OnServiceConnectedListener
   *                interface, for receiving connection notifications from the service.
   */
  public DisplayV3Connector(Context context, Account account, OnServiceConnectedListener client) {
    super(context, account, client);
  }

  /**
   * Null-safe wrapper around {@link FdParcelable#getValue()}. Call this over
   * {@link FdParcelable#getValue()} directly to protect against remote exceptions
   * resulting in null return values.
   *
   * @param fdp The {@link FdParcelable} on which to call {@link FdParcelable#getValue()}.
   * @param <V> The value type.
   * @param <P> The parcelable type.
   *
   * @return An object of type V, or null if the argument was null or the argument's
   * {@link FdParcelable#getValue()} method returned null.
   */
  private static <V, P extends FdParcelable<V>> V getValue(P fdp) {
    if (fdp == null) {
      return null;
    }
    return fdp.getValue();
  }

  @Override
  protected String getServiceIntentAction() {
    return DisplayIntent.ACTION_DISPLAY_SERVICE_V3;
  }

  @Override
  protected String getServiceIntentPackage() {
    return SERVICE_HOST;
  }

  @Override
  protected int getServiceIntentVersion() {
    return 3;
  }

  @Override
  protected IDisplayServiceV3 getServiceInterface(IBinder iBinder) {
    return IDisplayServiceV3.Stub.asInterface(iBinder);
  }

  protected void notifyServiceConnected(OnServiceConnectedListener client) {
    super.notifyServiceConnected(client);

    try {
      if (mListener == null) {
        mListener = new DisplayServiceListenerParent(this);
      }
      mService.addDisplayServiceListener(mListener);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void disconnect() {
    mDisplayServiceListener.clear();
    if (mListener != null) {
      if (mService != null) {
        try {
          mService.removeDisplayServiceListener(mListener);
        } catch (RemoteException e) {
          e.printStackTrace();
        }
      }
      mListener.destroy();
      mListener = null;
    }
    super.disconnect();
  }

  public void addDisplayServiceListener(DisplayServiceListener listener) {
    mDisplayServiceListener.add(new WeakReference<DisplayServiceListener>(listener));
  }

  public void removeDisplayServiceListener(DisplayServiceListener listener) {
    if (mDisplayServiceListener != null && !mDisplayServiceListener.isEmpty()) {
      WeakReference<DisplayServiceListener> listenerWeakReference = null;
      for (WeakReference<DisplayServiceListener> weakReference : mDisplayServiceListener) {
        DisplayServiceListener listener1 = weakReference.get();
        if (listener1 != null && listener1 == listener) {
          listenerWeakReference = weakReference;
          break;
        }
      }
      if (listenerWeakReference != null) {
        mDisplayServiceListener.remove(listenerWeakReference);
      }
    }
  }

  /**
   * Interface used to allow application to be notified of display updates.
   */
  public interface DisplayServiceListener {
    // Template for use in future.
    // public void onDisplayChanged(final DisplayChanged message);
  }

  private static class DisplayServiceListenerParent extends IDisplayServiceListener.Stub {
    private DisplayV3Connector mConnector;

    private DisplayServiceListenerParent(DisplayV3Connector connector) {
      mConnector = connector;
    }
    abstract class DisplayConnectorTask implements Runnable {
      private DisplayServiceListener listener;
      public DisplayServiceListener getListener() {
        return listener;
      }

      public void setListener(DisplayServiceListener listener) {
        this.listener = listener;
      }
    }
    private void conditionallyRunTask(DisplayConnectorTask task) {
      final DisplayV3Connector connector = mConnector;

      if (connector == null) {
        return; // Shouldn't get here, but bail just in case
      }
      if (connector.mDisplayServiceListener != null && !connector.mDisplayServiceListener.isEmpty()) {
        // NOTE: because of the use of CopyOnWriteArrayList, we *must* use an iterator to perform the dispatching. The
        // iterator is a safe guard against listeners that could mutate the list by calling the add/remove methods. This
        // array never changes during the lifetime of the iterator, so interference is impossible and the iterator is guaranteed
        // not to throw ConcurrentModificationException.
        for (WeakReference<DisplayServiceListener> weakReference : connector.mDisplayServiceListener) {
          final DisplayServiceListener listener = weakReference.get();
          if (listener != null) {
            task.setListener(listener);
            connector.mHandler.post(task);
          }
        }
      }
    }

    // Template for use in future.
    //    @Override
    //    public void onDisplayChanged(final DisplayChanged message) throws RemoteException {
    //      conditionallyRunTask(new DisplayConnectorTask() {
    //        @Override
    //        public void run() {
    //          getListener().onDisplayChanged(message);
    //        }
    //      });
    //    }

    // This method must be called when the callback is no longer needed to prevent a memory leak. Due to the design of
    // AIDL services Android unnecessarily retains pointers to otherwise unreferenced instances of this class which in
    // turn are referencing Context objects that consume large amounts of memory.
    public void destroy() {
      mConnector = null;
    }
  }
}
