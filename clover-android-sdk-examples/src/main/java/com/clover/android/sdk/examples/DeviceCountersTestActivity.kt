package com.clover.android.sdk.examples

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.clover.android.sdk.examples.databinding.ActivityDeviceCountersTestBinding
import kotlinx.coroutines.launch

class DeviceCountersTestActivity : AppCompatActivity() {

  private lateinit var binding: ActivityDeviceCountersTestBinding
  private val viewModel: DeviceCountersViewModel by viewModels()
  private val adapter = DeviceCountersAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDeviceCountersTestBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupRecyclerView()
    setupObservers()
    setupListeners()

    val defaultKey = "$packageName.test_counter"
    binding.editKey.setText(defaultKey)
    binding.editKey.setSelection(defaultKey.length)
  }

  override fun onStart() {
    super.onStart()
    viewModel.refreshCounters()
  }

  private fun setupRecyclerView() {
    binding.recyclerCounters.layoutManager = LinearLayoutManager(this)
    binding.recyclerCounters.adapter = adapter
    binding.recyclerCounters.addItemDecoration(
        DividerItemDecoration(
            this,
            DividerItemDecoration.VERTICAL
        )
    )
  }

  private fun setupListeners() {
    binding.buttonAdd.setOnClickListener {
      val key = binding.editKey.text.toString()
      val delta = binding.editDelta.text.toString().toLongOrNull() ?: 1L
      viewModel.add(key, delta)
    }
  }

  private fun setupObservers() {
    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch {
          viewModel.countersMap.collect { map ->
            adapter.submitList(map)
          }
        }

        launch {
          viewModel.status.collect { status ->
            if (status != null) {
              binding.textStatus.text = status
              binding.textStatus.visibility = View.VISIBLE
              if (status.contains("not available", ignoreCase = true)) {
                binding.layoutInputs.visibility = View.GONE
                binding.buttonAdd.visibility = View.GONE
              }
            } else {
              binding.textStatus.visibility = View.GONE
              binding.layoutInputs.visibility = View.VISIBLE
              binding.buttonAdd.visibility = View.VISIBLE
            }
          }
        }
      }
    }
  }
}
