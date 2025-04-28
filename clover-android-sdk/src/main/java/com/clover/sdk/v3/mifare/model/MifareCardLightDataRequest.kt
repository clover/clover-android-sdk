package com.clover.sdk.v3.mifare.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MifareCardLightDataRequest(val numBlocks: Int = 0): Parcelable
