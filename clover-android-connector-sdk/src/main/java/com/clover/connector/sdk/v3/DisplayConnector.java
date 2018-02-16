package com.clover.connector.sdk.v3;
/**
 * Copyright (C) 2016 Clover Network, Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v3.connector.IDeviceConnectorListener;
import com.clover.sdk.v3.connector.IDisplayConnector;
import com.clover.sdk.v3.connector.IDisplayConnectorListener;
import com.clover.sdk.v3.order.DisplayOrder;

import android.accounts.Account;
import android.content.Context;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;

public class DisplayConnector implements IDisplayConnector {

  private final DisplayV3Connector.DisplayServiceListener displayServiceListener = new DisplayV3Connector.DisplayServiceListener() {
    // Template for use in future.
    //    @Override
    //    public void onDisplayChanged(DisplayChanged message) {
    //      for (IDeviceConnectorListener listener:listeners) {
    //        ((IDisplayConnectorListener)listener).onDisplayChanged(message);
    //      }
    //    }
  };
  private DisplayV3Connector displayV3Connector;
  private AsyncTask waitingTask;
  private ArrayList<IDeviceConnectorListener> listeners = new ArrayList<>();

  /**
   * Constructs a new object. This class will use the {@link IDisplayServiceV3}
   * service.
   *
   * @param context the Context object, required for establishing a connection to
   *                the service.
   * @param account the Account to use with the service.
   * @param displayConnectorListener an optional object implementing the IDisplayConnectorListener
   */
  public DisplayConnector(Context context, Account account, IDisplayConnectorListener displayConnectorListener) {
    if (displayConnectorListener != null) {
      addCloverConnectorListener(displayConnectorListener);
    }
    connectToDisplayService(context, account);
  }

  private void connectToDisplayService(Context context, Account account) {
    if (displayV3Connector == null) {
      displayV3Connector = new DisplayV3Connector(context, account, new ServiceConnector.OnServiceConnectedListener() {
        @Override
        public void onServiceConnected(ServiceConnector connector) {
          Log.d(this.getClass().getSimpleName(), "onServiceConnected " + connector);
          displayV3Connector.addDisplayServiceListener(displayServiceListener);

          AsyncTask tempWaitingTask = waitingTask;
          waitingTask = null;

          if (tempWaitingTask != null) {
            tempWaitingTask.execute();
          }
          for (IDeviceConnectorListener listener : listeners) {
            listener.onDeviceConnected();
          }

        }

        @Override
        public void onServiceDisconnected(ServiceConnector connector) {
          Log.d(this.getClass().getSimpleName(), "onServiceDisconnected " + connector);
          for (IDeviceConnectorListener listener : listeners) {
            listener.onDeviceDisconnected();
          }
        }
      });
      displayV3Connector.connect();
    } else {
      if (!displayV3Connector.isConnected()) {
        displayV3Connector.connect();
      }
    }
  }

  /**
   * add an IDeviceConnectorListener to receive callbacks
   *
   * @param listener will listen to connection and disconnect events.
   */
  @Override
  public void addCloverConnectorListener(IDeviceConnectorListener listener) {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  /**
   * remove an ICloverConnectorListener from receiving callbacks
   *
   * @param listener will no longer listen to connection and disconnect events.
   */
  @Override
  public void removeCloverConnectorListener(IDeviceConnectorListener listener) {
    listeners.remove(listener);
  }

  /**
   * Initialize the DisplayConnector's connection. Must be called before calling any other
   * method other than to add or remove listeners
   */
  @Override
  public void initializeConnection() {
    displayV3Connector.connect();
  }

  /**
   * Closes the DisplayConnector's connection. Should be called after removing any listeners
   */
  @Override
  public void dispose() {
    try {
      if (displayV3Connector != null) {
        if (displayV3Connector.isConnected()) {
          displayV3Connector.getService().dispose();
          finishDispose();
        } else {
          if (this.displayV3Connector.connect()) {
            waitingTask = new AsyncTask() {
              @Override
              protected Object doInBackground(Object[] params) {
                try {
                  displayV3Connector.getService().dispose();
                } catch (RemoteException e) {
                  Log.e(this.getClass().getSimpleName(), " execute", e);
                }
                return null;
              }

              @Override
              protected void onPostExecute(Object unused) {
                finishDispose();
              }
            };
          } else {
            Log.e(this.getClass().getSimpleName(), " Failed to connect to display service", new Exception("Failed to connect to display service"));
          }
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " execute", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " execute", e);
    } finally {
      finishDispose();
    }
  }

  private void finishDispose() {
    if (displayV3Connector != null) {
      displayV3Connector.disconnect();
    }
    listeners.clear();
    displayV3Connector = null;
  }

  protected void execute(final VoidRun voidRun) {
    try {
      if (displayV3Connector != null) {
        if (displayV3Connector.isConnected()) {
          voidRun.run(displayV3Connector.getService());
        } else {
          if (this.displayV3Connector.connect()) {
            waitingTask = new AsyncTask() {
              @Override
              protected Object doInBackground(Object[] params) {
                try {
                  voidRun.run(displayV3Connector.getService());
                } catch (RemoteException e) {
                  Log.e(this.getClass().getSimpleName(), " execute", e);
                }
                return null;
              }
            };
          } else {
            Log.e(this.getClass().getSimpleName(), " Failed to connect to display service", new Exception("Failed to connect to display service"));
          }
        }
      }
    } catch (IllegalArgumentException e) {
      Log.e(this.getClass().getSimpleName(), " execute", e);
    } catch (RemoteException e) {
      Log.e(this.getClass().getSimpleName(), " execute", e);
    }
  }

  @Override
  public void showWelcomeScreen() {
    execute(new VoidRun() {
      @Override
      public void run(IDisplayServiceV3 service) throws RemoteException {
        service.showWelcomeScreen();
      }
    });
  }

  @Override
  public void showMessage(final String message) {
    execute(new VoidRun() {
      @Override
      public void run(IDisplayServiceV3 service) throws RemoteException {
        service.showMessage(message);
      }
    });
  }

  @Override
  public void showThankYouScreen() {
    execute(new VoidRun() {
      @Override
      public void run(IDisplayServiceV3 service) throws RemoteException {
        service.showThankYouScreen();
      }
    });
  }

  @Override
  public void showDisplayOrder(final DisplayOrder order) {
    execute(new VoidRun() {
      @Override
      public void run(IDisplayServiceV3 service) throws RemoteException {
        service.showDisplayOrder(order);
      }
    });
  }

  interface VoidRun {
    void run(IDisplayServiceV3 service) throws RemoteException;
  }
}
