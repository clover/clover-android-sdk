// ISelectApplicationHandler.aidl
package com.clover.sdk.v3.payment.raw.handler;

import com.clover.sdk.v3.payment.raw.model.CardApplication;

interface ISelectApplicationHandler {

        oneway void selectApplication(in CardApplication cardApplication);
}
