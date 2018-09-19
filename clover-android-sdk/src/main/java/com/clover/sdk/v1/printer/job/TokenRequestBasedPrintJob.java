package com.clover.sdk.v1.printer.job;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v3.payments.TokenRequest;

/**
 * To print a token Request or Card Verification receipt, create an instance of this print job and call print
 */

public class TokenRequestBasedPrintJob extends PrintJob implements Parcelable {

  public TokenRequest tokenRequest;
  public String reason;
  private static final String BUNDLE_TOKEN_REQUEST = "tokenRequest";
  private static final String REASON ="reason";

  public static class Builder extends PrintJob.Builder {
    protected TokenRequest tokenRequest;
    protected String reason;

    public Builder tokenRequest(TokenRequest tokenRequest) {
      this.tokenRequest = tokenRequest;
      return this;
    }

    public Builder reason(String reason) {
      this.reason = reason;
      return this;
    }

    @Override
    public PrintJob build() {
      return new TokenRequestBasedPrintJob(this);
    }
  }

  //Constructor to pass a builder with TokenRequest object
  protected TokenRequestBasedPrintJob(TokenRequestBasedPrintJob.Builder builder) {
    super(builder);
    this.tokenRequest = builder.tokenRequest;
    this.reason = builder.reason;
  }

  //Constructor to pass a parcel with TokenRequest object
  public TokenRequestBasedPrintJob(Parcel in) {
    super(in);
    Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader());
    tokenRequest = bundle.getParcelable(BUNDLE_TOKEN_REQUEST);
    reason = bundle.getString(REASON);
  }

  public static final Creator<TokenRequestBasedPrintJob> CREATOR
    = new Creator<TokenRequestBasedPrintJob>() {
    public TokenRequestBasedPrintJob createFromParcel(Parcel in) {
      return new TokenRequestBasedPrintJob(in);
    }

    public TokenRequestBasedPrintJob[] newArray(int size) {
      return new TokenRequestBasedPrintJob[size];
    }
  };

  @Override
  public Category getPrinterCategory() {
    return Category.RECEIPT;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    Bundle bundle = new Bundle();
    bundle.putParcelable(BUNDLE_TOKEN_REQUEST, tokenRequest);
    bundle.putString(REASON, reason);
    dest.writeBundle(bundle);
  }
}
