package com.clover.sdk.v3.payments.api;

/**
 * The result of the Closeout request from Payments API
 *
 * QUEUED -- the closeout request was successful and has been queued
 * FAILED -- the closeout request was unsuccessful for reasons other than open tips or preauths
 * OPEN_TIPS -- the closeout request was unsuccessful because there are open tips
 * OPEN_PREAUTHS -- the closeout request was unsuccessful because there are open preauths
 */
public class CloseoutResult {
    public static final String QUEUED = "QUEUED";
    public static final String FAILED = "FAILED";
    public static final String OPEN_TIPS = "OPEN_TIPS";
    public static final String OPEN_PREAUTHS = "OPEN_PREAUTHS";
}
