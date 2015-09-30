package com.clover.sdk.v1.tender;

import com.clover.sdk.v1.tender.Tender;
import com.clover.sdk.v1.ResultStatus;

/**
 * An interface for interacting with the Clover tender service. The tender
 * service is a bound AIDL service.
 */

 interface ITenderService {
    List<Tender> getTenders(out ResultStatus resultStatus);
    Tender checkAndCreateTender(String label, String labelKey, boolean enabled, boolean opensCashDrawer, out ResultStatus resultStatus);

    Tender setEnabled(in String tenderId, in boolean enabled, out ResultStatus resultStatus);

    void delete(in String tenderId, out ResultStatus resultStatus);

    void setOpensCashDrawer(in String tenderId, in boolean opensCashDrawer, out ResultStatus resultStatus);

    void setLabel(in String tenderId, in String tenderLable, out ResultStatus resultStatus);
}
