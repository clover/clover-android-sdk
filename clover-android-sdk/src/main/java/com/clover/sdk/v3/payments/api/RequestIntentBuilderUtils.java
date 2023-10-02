package com.clover.sdk.v3.payments.api;

import android.util.Log;
import com.clover.sdk.v1.Intents;

import java.util.Set;

public class RequestIntentBuilderUtils {

  public static int convert(Set<CardEntryMethod> methods) {
    if (methods == null || methods.isEmpty()) {
      return Intents.CARD_ENTRY_METHOD_ALL;
    }

    int result = 0;
    for (CardEntryMethod method : methods) {
      if (method != null) {
        switch (method) {
          case MANUAL:
            result |= Intents.CARD_ENTRY_METHOD_MANUAL;
            break;

          case MAG_STRIPE:
            result |= Intents.CARD_ENTRY_METHOD_MAG_STRIPE;
            break;

          case CHIP:
            result |= Intents.CARD_ENTRY_METHOD_ICC_CONTACT;
            break;

          case NFC:
            result |= Intents.CARD_ENTRY_METHOD_NFC_CONTACTLESS;
            break;

          default:
            Log.w(RequestIntentBuilderUtils.class.getSimpleName(), "Unknown card entry method: " + method);
            break;
        }
      }
    }
    return result != 0 ? result : Intents.CARD_ENTRY_METHOD_ALL;
  }


}
