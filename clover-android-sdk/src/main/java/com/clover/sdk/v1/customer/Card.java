package com.clover.sdk.v1.customer;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

public class Card implements Parcelable {
  public static class Builder {
    private String id = null;
    private String first6 = null;
    private String last4 = null;
    private String expirationDate = null;
    private String cardType = null;
    private String firstName = null;
    private String lastName = null;
    private String token = null;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder first6(String first6) {
      this.first6 = first6;
      return this;
    }

    public Builder last4(String last4) {
      this.last4 = last4;
      return this;
    }

    public Builder expirationDate(String expirationDate) {
      this.expirationDate = expirationDate;
      return this;
    }

    public Builder cardType(String cardType) {
      this.cardType = cardType;
      return this;
    }

    public Builder firstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public Builder lastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    public Builder token(String token) {
      this.token = token;
      return this;
    }

    public Card build() {
      return new Card(id, first6, last4, expirationDate, cardType, firstName, lastName, token);
    }
  }

  private final JSONObject data;

  private Card(String id, String first6, String last4, String expirationDate, String cardType,
               String firstName, String lastName, String token) {
    data = new JSONObject();
    try {
      data.put("id", id);
      data.put("first6", first6);
      data.put("last4", last4);
      data.put("expirationDate", expirationDate);
      data.put("cardType", cardType);
      data.put("firstName", firstName);
      data.put("lastName", lastName);
      data.put("token", token);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  Card(String json) throws JSONException {
    this.data = new JSONObject(json);
  }

  Card(Parcel in) throws JSONException {
    String json = in.readString();
    this.data = new JSONObject(json);
  }

  public Card(JSONObject data) {
    this.data = data;
  }

  public String getId() {
    return data.optString("id", null);
  }
  public String getFirst6() {
    return data.optString("first6", null);
  }
  public String getLast4() {
    return data.optString("last4", null);
  }
  public String getExpirationDate() {
    return data.optString("expirationDate", null);
  }
  public String getCardType() {
    return data.optString("cardType", null);
  }
  public String getFirstName() {
    return data.optString("firstName", null);
  }
  public String getLastName() {
    return data.optString("lastName", null);
  }
  public String getToken() {
    return data.optString("token", null);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    String json = data.toString();
    dest.writeString(json);
  }

  public static final Creator<Card> CREATOR = new Creator<Card>() {
    public Card createFromParcel(Parcel in) {
      try {
        return new Card(in);
      } catch (JSONException e) {
        throw new RuntimeException(e);
      }
    }

    public Card[] newArray(int size) {
      return new Card[size];
    }
  };
}