package com.clover.sdk.cfp.connector.session;

public interface CFPSessionListener {
  void onSessionDataChanged(String type, Object data);
  void onSessionEvent(String type, String data);
}