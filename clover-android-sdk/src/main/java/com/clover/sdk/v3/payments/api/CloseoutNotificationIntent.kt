package com.clover.sdk.v3.payments.api

/**
 * This class defines the intent for receiving closeout notifications.
 * Apps that want to handle closeout notifications should listen for this intent.
 */
object CloseoutNotificationIntent {
  /**
   * This intent is handled by apps that want to receive closeout notifications.
   * It includes the [.EXTRA_BATCH_ID] and [.EXTRA_SUCCESS] extras.
   */
  const val ACTION_BATCH_CLOSED_NOTIFICATION: String = "clover.intent.action.BATCH_CLOSED_INTENT_ACTION"

  /**
   * A required `String` extra sent with an [.ACTION_BATCH_CLOSED_NOTIFICATION] intent
   * that specifies the closeout batch ID.
   */
  const val EXTRA_BATCH_ID: String = "batchId"

  /**
   * An optional `Boolean` extra sent with an [.ACTION_BATCH_CLOSED_NOTIFICATION] intent
   * that is true if the closeout was successful and false if it was not.
   */
  const val EXTRA_SUCCESS: String = "success"
}
