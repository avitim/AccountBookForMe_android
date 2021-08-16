package com.example.accountbookforme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.databinding.FragmentExpensePaymentBinding
import com.example.accountbookforme.model.Payment
import com.example.accountbookforme.viewmodel.PaymentViewModel

class ExpensePaymentListAdapter(private val paymentViewModel: PaymentViewModel): ListAdapter<Payment, ExpensePaymentListAdapter.ExpensePaymentViewHolder>(DiffCallbackPayment) {

    private lateinit var listener: OnExpensePaymentClickListener

    // 結果を渡すリスナー
    interface OnExpensePaymentClickListener {
        fun onItemClick(payment: Payment)
    }

    // リスナーをセット
    fun setOnExpensePaymentClickListener(listener: OnExpensePaymentClickListener) {
        this.listener = listener
    }

    open class ExpensePaymentViewHolder(private val binding: FragmentExpensePaymentBinding, private val paymentViewModel: PaymentViewModel): RecyclerView.ViewHolder(binding.root) {

        fun bind(payment: Payment) {
            binding.name.text = paymentViewModel.getNameById(payment.paymentId)
            binding.total.text = payment.total.toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpensePaymentViewHolder {
        val binding = FragmentExpensePaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpensePaymentViewHolder(binding, paymentViewModel)
    }

    override fun onBindViewHolder(holder: ExpensePaymentViewHolder, position: Int) {
        holder.bind(getItem(position))

        // セルのクリックイベントをセット
        holder.itemView.setOnClickListener {
            listener.onItemClick(getItem(position))
        }
    }
}

private object DiffCallbackPayment: DiffUtil.ItemCallback<Payment>() {

    override fun areItemsTheSame(oldItem: Payment, newItem: Payment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Payment, newItem: Payment): Boolean {
        return oldItem == newItem
    }
}