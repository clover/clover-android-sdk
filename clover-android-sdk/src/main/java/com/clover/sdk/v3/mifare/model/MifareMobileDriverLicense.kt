package com.clover.sdk.v3.mifare.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MifareMobileDriverLicense(val mdlNfcData: String?, val mdlDeviceEngagementData: String?): Parcelable
