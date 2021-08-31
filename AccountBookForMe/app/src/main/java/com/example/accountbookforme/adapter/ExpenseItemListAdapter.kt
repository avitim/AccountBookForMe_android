package com.example.accountbookforme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.databinding.NormalListItemBinding
import com.example.accountbookforme.database.entity.ItemEntity
import com.example.accountbookforme.util.Utils
import com.example.accountbookforme.viewmodel.CategoriesViewModel

class ExpenseItemListAdapter(private val categoriesViewModel: CategoriesViewModel) :
    ListAdapter<ItemEntity, ExpenseItemListAdapter.ExpenseItemViewHolder>(DiffCallback) {

    private lateinit var listener: OnExpenseItemClickListener

    // 結果を渡すリスナー
    interface OnExpenseItemClickListener {
        fun onItemClick(position: Int, item: ItemEntity)
    }

    // リスナーをセット
    fun setOnExpenseItemClickListener(listener: OnExpenseItemClickListener) {
        this.listener = listener
    }

    open class ExpenseItemViewHolder(
        private val binding: NormalListItemBinding,
        private val categoriesViewModel: CategoriesViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemEntity) {
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

private object DiffCallback : DiffUtil.ItemCallback<ItemEntity>() {

    override fun areItemsTheSame(oldItem: ItemEntity, newItem: ItemEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ItemEntity, newItem: ItemEntity): Boolean {
        return oldItem == newItem
    }
}