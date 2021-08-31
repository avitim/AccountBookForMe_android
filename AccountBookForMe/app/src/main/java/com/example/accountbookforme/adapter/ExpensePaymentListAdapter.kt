package com.example.accountbookforme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.database.entity.ExpensePaymentEntity
import com.example.accountbookforme.databinding.SimpleListItemBinding
import com.example.accountbookforme.util.Utils
import com.example.accountbookforme.viewmodel.PaymentsViewModel

class ExpensePaymentListAdapter(private val paymentsViewModel: PaymentsViewModel) :
    ListAdapter<ExpensePaymentEntity, ExpensePaymentListAdapter.ExpensePaymentViewHolder>(PaymentDiffCallback) {

    private lateinit var listener: OnExpensePaymentClickListener

    // 結果を渡すリスナー
    interface OnExpensePaymentClickListener {
        fun onItemClick(position: Int, ep: ExpensePaymentEntity)
    }

    // リスナーをセット
    fun setOnExpensePaymentClickListener(listener: OnExpensePaymentClickListener) {
        this.listener = listener
    }

    open class ExpensePaymentViewHolder(
        private val binding: SimpleListItemBinding,
        private val paymentsViewModel: PaymentsViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ep: ExpensePaymentEntity) {
            binding.label.text = paymentsViewModel.getById(ep.paymentId)?.name
            binding.value.text = Utils.convertToStrDecimal(ep.total)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpensePaymentViewHolder {
        val binding = SimpleListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpensePaymentViewHolder(binding, paymentsViewModel)
    }

    override fun onBindViewHolder(holder: ExpensePaymentViewHolder, position: Int) {
        holder.bind(getItem(position))

        // セルのクリックイベントをセット
        holder.itemView.setOnClickListener {
            listener.onItemClick(position, getItem(position))
        }
    }
}

private object PaymentDiffCallback : DiffUtil.ItemCallback<ExpensePaymentEntity>() {

    override fun areItemsTheSame(oldItem: ExpensePaymentEntity, newItem: ExpensePaymentEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ExpensePaymentEntity, newItem: ExpensePaymentEntity): Boolean {
        return oldItem == newItem
    }
}