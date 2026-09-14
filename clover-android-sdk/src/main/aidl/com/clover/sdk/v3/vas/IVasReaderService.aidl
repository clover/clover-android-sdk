package com.clover.sdk.v3.vas;

import com.clover.sdk.v3.payments.IVasProvider;
import com.clover.sdk.v3.payments.VasSettings;
import com.clover.sdk.v3.payments.VasPayloadResponse;
import com.clover.sdk.v3.vas.IVasReaderSessionListener;

import android.os.IBinder;

/**
 * An interface for interacting with the Clover VAS (Value Added
 * Services) Reader Service.
 * <p>
 * Interact with this service through the {@link VasReaderClient}
 * class, which manages session lifecycle and binding automatically.
 * <p>
 * All methods require APPLE_VAS_R permission. Applications must be submitted
 * to Clover and have APPLE_VAS_R granted before they can bind to this
 * service.
 */
interface IVasReaderService {

  /**
   * Open a VAS reader session with the given providers.
   *
   * @param vasProviderBinders List of IBinders for IVasProvider implementations.
   * @param sessionListener    Callback for session-level events.
   * @return true if the session was opened successfully, false otherwise.
   * @clover.perm APPLE_VAS_R
   */
 boolean openSession(in List<IBinder> vasProviderBinders, in IVasReaderSessionListener sessionListener);

 /**
  * Resets the active session timeout without reconnecting or reinjecting providers.
  * @clover.perm APPLE_VAS_R
  */
 void resetTimeout();

  /**
   * Cancel any in-progress VAS read operation.
   *
   * @clover.perm APPLE_VAS_R
   */
 void cancel();

    /**
     * Close the current VAS reader session and release hardware resources.
     *
     * @clover.perm APPLE_VAS_R
     */
 void closeSession();

  /**
   * Read VAS payload from a mobile device or card using the provided
   * settings. Blocks until a payload is read or the operation is
   * cancelled.
   *
   * @param vasSettings Configuration for the VAS read operation.
   * @return A {@link VasPayloadResponse} if data was read, or null if
   *         the read was cancelled or failed.
   * @clover.perm APPLE_VAS_R
   */
 VasPayloadResponse startVasRead(in VasSettings vasSettings);
}