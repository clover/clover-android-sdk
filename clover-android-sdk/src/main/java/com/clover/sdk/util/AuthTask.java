/**
 * Copyright (C) 2015 Clover Network, Inc.
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
package com.clover.sdk.util;

import android.accounts.Account;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.AsyncTask;

import java.io.IOException;

/**
 * Authenticates with the Clover service in the background.  Subclasses can override
 * callback methods in order to respond to success or failure.
 */
public class AuthTask extends AsyncTask<Account, Void, CloverAuth.AuthResult> {

  private final Activity activity;
  private OperationCanceledException canceledException;
  private AuthenticatorException authException;
  private IOException ioException;
  private Exception exception;
  private String errorMessage;

  /**
   * Create a new {@code AuthTask}.
   *
   * @param activity the {@code Activity} that initiated authentication with the Clover service.
   */
  public AuthTask(Activity activity) {
    this.activity = activity;
  }

  /**
   * Return the {@code Exception} that was thrown when authentication failed, or {@code null}
   * if authentication succeeded.
   */
  public Exception getException() {
    return exception;
  }

  /**
   * Return the error message that was generated when authentication failed, or {@code null}
   * if authentication succeeded.
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  @Override
  protected CloverAuth.AuthResult doInBackground(Account... accounts) {
    CloverAuth.AuthResult result = null;
    try {
      result = CloverAuth.authenticate(activity, accounts[0]);
      errorMessage = result.errorMessage;
    } catch (OperationCanceledException e) {
      canceledException = e;
      exception = e;
      errorMessage = getErrorMessage(e);
    } catch (AuthenticatorException e) {
      authException = e;
      exception = e;
      errorMessage = getErrorMessage(e);
    } catch (IOException e) {
      ioException = e;
      exception = e;
      errorMessage = getErrorMessage(e);
    }
    return result;
  }

  private static String getErrorMessage(Exception e) {
    String message = e.getMessage();
    return (message != null ? message : e.toString());
  }

  /**
   * Calls callback methods after the authentication attempt completes.  If a subclass
   * overrides this method, it should call the parent implementation.  Otherwise the callback methods
   * will not be called.
   */
  @Override
  protected void onPostExecute(CloverAuth.AuthResult result) {
    super.onPostExecute(result);

    if (canceledException != null) {
      onAuthCanceled(canceledException);
      onAuthComplete(false, result);
    } else if (authException != null) {
      onAuthenticatorException(authException);
      onAuthComplete(false, result);
    } else if (ioException != null) {
      onIOException(ioException);
      onAuthComplete(false, result);
    } else {
      onAuthComplete(result.authToken != null, result);
    }
  }

  /**
   * Called on the main thread after the auth process has completed.
   *
   * @param success {@code true} if authentication was successful
   * @param result  the authentication result, or {@code null} if an exception was thrown
   */
  protected void onAuthComplete(boolean success, CloverAuth.AuthResult result) {
  }

  /**
   * Called on the main thread if {@link android.accounts.OperationCanceledException} was
   * thrown during authentication.
   */
  protected void onAuthCanceled(OperationCanceledException e) {
  }

  /**
   * Called on the main thread if {@link android.accounts.AuthenticatorException} was
   * thrown during authentication.
   */
  protected void onAuthenticatorException(AuthenticatorException e) {
  }

  /**
   * Called on the main thread if {@link java.io.IOException} was thrown during authentication.
   */
  protected void onIOException(IOException e) {
  }
}
