package com.example.accountbookforme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.databinding.NormalListItemBinding
import com.example.accountbookforme.model.Item
import com.example.accountbookforme.util.Utils
import com.example.accountbookforme.viewmodel.CategoriesViewModel

class ExpenseItemListAdapter(private val categoriesViewModel: CategoriesViewModel) :
    ListAdapter<Item, ExpenseItemListAdapter.ExpenseItemViewHolder>(DiffCallback) {

    private lateinit var listener: OnExpenseItemClickListener

    // 結果を渡すリスナー
    interface OnExpenseItemClickListener {
        fun onItemClick(position: Int, item: Item)
    }

    // リスナーをセット
    fun setOnExpenseItemClickListener(listener: OnExpenseItemClickListener) {
        this.listener = listener
    }

    open class ExpenseItemViewHolder(
        private val binding: NormalListItemBinding,
        private val categoriesViewModel: CategoriesViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.label.text = item.name
            binding.subLabel.text = categoriesViewModel.getById(item.categoryId)?.name
            binding.value.text = Utils.convertToStrDecimal(item.price)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpenseItemViewHolder {
        val binding =
            NormalListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseItemViewHolder(binding, categoriesViewModel)
    }

    override fun onBindViewHolder(holder: ExpenseItemViewHolder, position: Int) {

        holder.bind(getItem(position))

        // セルのクリックイベントをセット
        holder.itemView.setOnClickListener {
            listener.onItemClick(position, getItem(position))
        }
    }
}

private object DiffCallback : DiffUtil.ItemCallback<Item>() {

    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}