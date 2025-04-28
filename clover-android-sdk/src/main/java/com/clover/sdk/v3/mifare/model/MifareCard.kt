package com.clover.sdk.v3.mifare.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MifareCard(val uid: MifareCardUuid?, val cardData: String?, val cardType: MifareCardType?) : Parcelable
