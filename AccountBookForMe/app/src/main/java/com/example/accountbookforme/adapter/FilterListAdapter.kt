package com.example.accountbookforme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.databinding.SingleListItemBinding
import com.example.accountbookforme.model.Filter

class FilterListAdapter :
    ListAdapter<Filter, FilterListAdapter.FilterViewHolder>(FilterDiffCallback) {

    private lateinit var listener: OnFilterClickListener

    // 結果を渡すリスナー
    interface OnFilterClickListener {
        fun onItemClick(filter: Filter)
    }

    // リスナーをセット
    fun setOnFilterClickListener(listener: OnFilterClickListener) {
        this.listener = listener
    }

    open class FilterViewHolder(
        private val binding: SingleListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(filter: Filter) {
            binding.label.text = filter.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilterViewHolder {
        val binding =
            SingleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {

        holder.bind(getItem(position))

        // セルのクリックイベントをセット
        holder.itemView.setOnClickListener {
            listener.onItemClick(getItem(position))
        }
    }
}

private object FilterDiffCallback : DiffUtil.ItemCallback<Filter>() {

    override fun areItemsTheSame(oldItem: Filter, newItem: Filter): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Filter, newItem: Filter): Boolean {
        return oldItem == newItem
    }
}
