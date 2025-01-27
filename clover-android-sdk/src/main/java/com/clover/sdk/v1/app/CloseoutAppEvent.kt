package com.clover.sdk.v1.app

/**
 * Constants used in batch closeout result notifications
 *
 * When a closeout batch completes, the server sends push notifications
 * to the merchant's devices. These are processed in the engine app,
 * which then issues an app notification (see [AppNotificationIntent])
 * with the success/failure of the closeout batch, along with the Batch Id.
 */
enum class CloseoutAppEvent(val appEvent: String) {
  BATCH_CLOSED("batch_closed"),
  BATCH_FAILED("batch_failed"),
  EXTRA_BATCH_ID("batchId")
}