package com.clover.sdk.v3.mifare.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MifareMobileDriverLicenseRequest(val cardBleUuid: String): Parcelable
