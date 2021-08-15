package com.example.accountbookforme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.databinding.FragmentExpenseItemBinding
import com.example.accountbookforme.model.Item

class ExpenseItemAdapter: ListAdapter<Item, ExpenseItemAdapter.ExpenseItemViewHolder>(DiffCallback) {

    private lateinit var listener: OnExpenseItemClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpenseItemViewHolder {
        val binding = FragmentExpenseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseItemViewHolder, position: Int) {
        holder.bind(getItem(position))

        // セルのクリックイベントをセット
        holder.itemView.setOnClickListener {
            listener.onItemClick(getItem(position))
        }
    }

    // リスナーのインターフェース
    interface OnExpenseItemClickListener {
        fun onItemClick(item: Item)
    }

    // リスナーをセット
    fun setOnExpenseItemClickListener(listener: OnExpenseItemClickListener) {
        this.listener = listener
    }

    open class ExpenseItemViewHolder(private val binding: FragmentExpenseItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.name.text = item.name
            binding.category.text = item.categoryId.toString()
            binding.price.text = item.price.toString()
        }
    }

}

private object DiffCallback: DiffUtil.ItemCallback<Item>() {

    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}