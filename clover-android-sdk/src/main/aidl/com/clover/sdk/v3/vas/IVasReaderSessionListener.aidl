package com.clover.sdk.v3.vas;

interface IVasReaderSessionListener {
    void onUserInterventionRequired();
    void onUserInterventionCleared();
    void onVasReadTimeout();
}