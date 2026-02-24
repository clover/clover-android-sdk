package com.clover.android.sdk.examples.nfc

enum class NfcReaderOperationIds(val type: Int) {
    FELICA_UUID(0),
    FELICA_COMMAND(1),
    CANCEL(6)
}