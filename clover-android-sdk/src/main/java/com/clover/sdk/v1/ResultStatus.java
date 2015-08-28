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
package com.clover.sdk.v1;

import android.os.Parcel;
import android.os.Parcelable;
import com.clover.core.internal.Objects;

/**
 * Possible statuses results from calling Clover services. Most Clover service calls accept
 * an instance of this class as an "out" parameter. For example,
 * <pre>
 * <code>
 * ResultStatus resultStatus = new ResultStatus();
 * Merchant m = iMerchantService.getMerchant(resultStatus);
 * if (resultStatus.isSuccess()) {
 *   ...
 * }
 * </code>
 * </pre>
 */
public class ResultStatus implements Parcelable {
  public static final int UNKNOWN = -1;
  public static final int OK = 200;
  public static final int OK_CREATED = 201;
  public static final int OK_ACCEPTED = 202;
  public static final int OK_NON_AUTHORITATIVE = 203;
  public static final int BAD_REQUEST = 400;
  public static final int UNAUTHORIZED = 401;
  public static final int FORBIDDEN = 403;
  public static final int NOT_FOUND = 404;
  public static final int REQUEST_TOO_LARGE = 413;
  public static final int RESPONSE_TOO_LARGE = 421;
  public static final int SERVICE_ERROR = 500;
  public static final int NOT_IMPLEMENTED = 501;
  public static final int OTHER = 999;

  private int statusCode = UNKNOWN;
  private String statusMessage;

  public ResultStatus() {
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public String getStatusMessage() {
    return statusMessage;
  }

  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
  }

  public void setStatus(int statusCode, String statusMessage) {
    this.statusCode = statusCode;
    this.statusMessage = statusMessage;
  }

  public boolean isSuccess() {
    return statusCode >= 200 && statusCode <= 299;
  }

  public boolean isClientError() {
    return (statusCode >= 400 && statusCode <= 499) || statusCode == 999;
  }

  public boolean isServiceError() {
    return statusCode >= 500 && statusCode <= 599;
  }

  private static String getStatusCodeString(int statusCode) {
    switch (statusCode) {
      case UNKNOWN:
        return "UNKNOWN";
      case OK:
        return "OK";
      case OK_CREATED:
        return "OK_CREATED";
      case OK_ACCEPTED:
        return "OK_ACCEPTED";
      case OK_NON_AUTHORITATIVE:
        return "OK_NON_AUTHORITATIVE";
      case BAD_REQUEST:
        return "BAD_REQUEST";
      case UNAUTHORIZED:
        return "UNAUTHORIZED";
      case FORBIDDEN:
        return "FORBIDDEN";
      case NOT_FOUND:
        return "NOT_FOUND";
      case REQUEST_TOO_LARGE:
        return "REQUEST_TOO_LARGE";
      case RESPONSE_TOO_LARGE:
        return "RESPONSE_TOO_LARGE";
      case SERVICE_ERROR:
        return "SERVICE_ERROR";
      case NOT_IMPLEMENTED:
        return "NOT_IMPLEMENTED";
      case OTHER:
        return "CLOVER_ERROR";
      default:
        return "Unknown status code; getStatusCodeString() needs to be updated";
    }
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this).add("statusCode", getStatusCodeString(statusCode)).add("statusMessage", statusMessage).toString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeInt(statusCode);
    out.writeString(statusMessage);
  }

  public void readFromParcel(Parcel in) {
    statusCode = in.readInt();
    statusMessage = in.readString();
  }

  public static final android.os.Parcelable.Creator<ResultStatus> CREATOR = new android.os.Parcelable.Creator<ResultStatus>() {
    public ResultStatus createFromParcel(android.os.Parcel in) {
      ResultStatus resultStatus = new ResultStatus();
      resultStatus.readFromParcel(in);
      return resultStatus;
    }

    public ResultStatus[] newArray(int size) {
      return new ResultStatus[size];
    }
  };
}
