package com.example.marketmind.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.marketmind.databinding.ItemRiskMetricBinding
import com.example.marketmind.domain.model.RiskMetric

class RiskMetricAdapter :
    ListAdapter<RiskMetric, RiskMetricAdapter.RiskMetricViewHolder>(RiskMetricDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiskMetricViewHolder {
        val binding = ItemRiskMetricBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RiskMetricViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RiskMetricViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RiskMetricViewHolder(private val binding: ItemRiskMetricBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RiskMetric) {
            binding.tvMetricName.text = item.name
            binding.tvMetricValue.text = item.value

            binding.ivInfo.setOnClickListener {
                item.description?.let { desc ->
                    AlertDialog.Builder(binding.root.context)
                        .setTitle(item.name)
                        .setMessage(desc)
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
        }
    }

    private class RiskMetricDiffCallback : DiffUtil.ItemCallback<RiskMetric>() {
        override fun areItemsTheSame(oldItem: RiskMetric, newItem: RiskMetric): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: RiskMetric, newItem: RiskMetric): Boolean {
            return oldItem == newItem
        }
    }
}