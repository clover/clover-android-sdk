package com.clover.sdk.v1.printer;

import com.clover.android.sdk.R;

import android.content.Context;

public enum DeclineReason {
  DECLINE(R.string.decline_reason_decline),
  VOID(R.string.decline_reason_void),
  CANCEL(R.string.decline_reason_cancel),
  REFERRAL(R.string.decline_reason_referral),
  VOIDING(R.string.decline_reason_voiding);

  private final int msgId;

  DeclineReason(int msgId) {
    this.msgId = msgId;
  }

  public String getMessage(Context context) {
    return context.getString(msgId);
  }
}
