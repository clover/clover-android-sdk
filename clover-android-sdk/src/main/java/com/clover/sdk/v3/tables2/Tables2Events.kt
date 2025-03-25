package com.clover.sdk.v3.tables2

/**
 * [Tables2EventsContract.Tables2EventsColumns.EVENT_NAME]s for different dining events
 *
 * * [MOVE_ORDER_FROM_TABLE_TO_TABLE]: When an order is moved from one table to other table
 * * [MOVE_ORDER_FROM_TABLE_TO_BAR_AND_MORE]: When an order is moved from a table to Bar & More
 * * [MOVE_ORDER_FROM_BAR_AND_MORE_TO_TABLE]: When an order is moved from Bar & More to a table
 * * [COMBINE_ORDERS]: When any two orders are combined (including table order(s) or Bar order(s))
 * * [MOVE_GUEST_TO_TABLE]: When a guest is moved to other table
 * * [MOVE_GUEST_TO_BAR_AND_MORE]: When a guest is moved to Bar & More
 * * [MOVE_WHOLE_TABLE_ITEMS_TO_GUEST]: When whole table item(s) are moved to a guest
 * * [MOVE_GUEST_ITEMS_TO_WHOLE_TABLE]: When guest item(s) are moved to whole table
 * * [SPLIT_WHOLE_TABLE_ITEMS_BETWEEN_GUESTS]: When splitting item(s) between guest
 * * [MOVE_ITEMS_BETWEEN_GUESTS]: When moving item(s) between guests
 * * [CREATE_GUEST_ORDER]: When paying for individual/multiple guest(s)
 * * [REVERT_GUEST_ORDER_CREATION]: When paying for individual/multiple guest(s) is reverted/cancelled
 *
 * @see Tables2EventsContract
 * */
object Tables2Events {
  const val MOVE_ORDER_FROM_TABLE_TO_TABLE = "MOVE_ORDER_FROM_TABLE_TO_TABLE"
  const val MOVE_ORDER_FROM_TABLE_TO_BAR_AND_MORE = "MOVE_ORDER_FROM_TABLE_TO_BAR_AND_MORE"
  const val MOVE_ORDER_FROM_BAR_AND_MORE_TO_TABLE = "MOVE_ORDER_FROM_BAR_AND_MORE_TO_TABLE"
  const val COMBINE_ORDERS = "COMBINE_ORDERS"
  const val MOVE_GUEST_TO_TABLE = "MOVE_GUEST_TO_TABLE"
  const val MOVE_GUEST_TO_BAR_AND_MORE = "MOVE_GUEST_TO_BAR_AND_MORE"
  const val MOVE_WHOLE_TABLE_ITEMS_TO_GUEST = "MOVE_WHOLE_TABLE_ITEMS_TO_GUEST"
  const val MOVE_GUEST_ITEMS_TO_WHOLE_TABLE = "MOVE_GUEST_ITEMS_TO_WHOLE_TABLE"
  const val SPLIT_WHOLE_TABLE_ITEMS_BETWEEN_GUESTS = "SPLIT_WHOLE_TABLE_ITEMS_BETWEEN_GUESTS"
  const val MOVE_ITEMS_BETWEEN_GUESTS = "MOVE_ITEMS_BETWEEN_GUESTS"
  const val CREATE_GUEST_ORDER = "CREATE_GUEST_ORDER"
  const val REVERT_GUEST_ORDER_CREATION = "REVERT_GUEST_ORDER_CREATION"
}