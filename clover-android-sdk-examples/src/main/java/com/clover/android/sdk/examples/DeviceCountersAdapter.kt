package com.clover.android.sdk.examples

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat

class DeviceCountersAdapter :
    ListAdapter<Pair<String, Long>, DeviceCountersAdapter.ViewHolder>(DiffCallback) {

  private val numberFormat = NumberFormat.getInstance()

  fun submitList(map: Map<String, Long>) {
    submitList(map.toList().sortedBy { it.first })
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.item_device_counter, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val (key, value) = getItem(position)
    holder.bind(key, value, numberFormat)
  }

  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textKey: TextView = itemView.findViewById(R.id.text_key)
    private val textValue: TextView = itemView.findViewById(R.id.text_value)

    fun bind(key: String, value: Long, numberFormat: NumberFormat) {
      textKey.text = key
      textValue.text = numberFormat.format(value)
    }
  }

  object DiffCallback : DiffUtil.ItemCallback<Pair<String, Long>>() {
    override fun areItemsTheSame(
      oldItem: Pair<String, Long>,
      newItem: Pair<String, Long>
    ) = oldItem.first == newItem.first

    override fun areContentsTheSame(
      oldItem: Pair<String, Long>,
      newItem: Pair<String, Long>
    ) = oldItem == newItem
  }
}
