package com.clover.sdk.v3.nfc.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FelicaCardCommand(
    val commandDataInHex: String
) : Parcelable
