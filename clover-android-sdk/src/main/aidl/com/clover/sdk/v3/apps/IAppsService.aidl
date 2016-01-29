package com.clover.sdk.v3.apps;

import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v3.apps.App;
import com.clover.sdk.v3.apps.AppBillingInfo;

/**
    Provides services related to Clover applications.
 */
interface IAppsService {

    App getApp(out ResultStatus resultStatus);

    void logMetered(String meteredId, int numberOfEvent, out ResultStatus resultStatus);

    AppBillingInfo getAppBillingInfo(out ResultStatus resultStatus);

    void setSmartReceiptText(String text, out ResultStatus resultStatus);

    void setSmartReceiptUrl(String url, out ResultStatus resultStatus);
}
