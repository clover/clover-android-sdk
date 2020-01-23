package com.clover.connector.sdk.v3.session;

public interface SessionListener {
  void onSessionDataChanged(String type, Object data);
  void onSessionEvent(String type, String data);
}