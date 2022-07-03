package com.vladhanin.tapmobiletestapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

data class ResultUiEntity(
    val id: String,
    val thumbnailUrl: String
)

class ResultsRecyclerViewAdapter : ListAdapter<ResultUiEntity, ResultsRecyclerViewAdapter.ResultViewHolder>(ResultsDiffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder =
        ResultViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_result, parent, false))

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(getItem(position).thumbnailUrl)
    }

    class ResultViewHolder (private val view: View) : RecyclerView.ViewHolder(view) {
        private val thumbnailImageView = view.findViewById<TextView>(R.id.thumbnailImageView)

        fun bind(thumbnailUrl: String) {
        }
    }

    object ResultsDiffItemCallback:  DiffUtil.ItemCallback<ResultUiEntity>() {
        override fun areItemsTheSame(oldItem: ResultUiEntity, newItem: ResultUiEntity): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ResultUiEntity, newItem: ResultUiEntity): Boolean =
            oldItem == newItem
    }
}