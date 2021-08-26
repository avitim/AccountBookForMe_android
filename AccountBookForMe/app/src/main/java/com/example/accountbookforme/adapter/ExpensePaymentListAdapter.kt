package com.example.accountbookforme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.databinding.SimpleListItemBinding
import com.example.accountbookforme.model.Payment
import com.example.accountbookforme.util.Utils
import com.example.accountbookforme.viewmodel.PaymentsViewModel

class ExpensePaymentListAdapter(private val paymentsViewModel: PaymentsViewModel) :
    ListAdapter<Payment, ExpensePaymentListAdapter.ExpensePaymentViewHolder>(PaymentDiffCallback) {

    private lateinit var listener: OnExpensePaymentClickListener

    // 結果を渡すリスナー
    interface OnExpensePaymentClickListener {
        fun onItemClick(position: Int, payment: Payment)
    }

    // リスナーをセット
    fun setOnExpensePaymentClickListener(listener: OnExpensePaymentClickListener) {
        this.listener = listener
    }

    open class ExpensePaymentViewHolder(
        private val binding: SimpleListItemBinding,
        private val paymentsViewModel: PaymentsViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(payment: Payment) {
            binding.label.text = paymentsViewModel.getNameById(payment.paymentId)
            binding.value.text = Utils.convertToStrDecimal(payment.total)
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

private object PaymentDiffCallback : DiffUtil.ItemCallback<Payment>() {

    override fun areItemsTheSame(oldItem: Payment, newItem: Payment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Payment, newItem: Payment): Boolean {
        return oldItem == newItem
    }
}