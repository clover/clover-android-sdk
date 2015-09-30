package com.clover.sdk.v1.app;

import com.clover.sdk.v1.app.AppNotification;
import com.clover.sdk.v1.ResultStatus;

/**
    Provides services related to Clover applications.
 */
interface IAppService {

    /**
        Send a notification to all instances of this app running at the merchant site.
        The notification is queued and sent in the background, so it's safe to make this
        call on the main thread.
    */
    void notify(in AppNotification notification, out ResultStatus resultStatus);
  }
