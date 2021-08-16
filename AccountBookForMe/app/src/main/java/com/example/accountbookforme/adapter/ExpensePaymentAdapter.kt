package com.example.accountbookforme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.databinding.FragmentExpensePaymentBinding
import com.example.accountbookforme.model.Payment

class ExpensePaymentAdapter: ListAdapter<Payment, ExpensePaymentAdapter.ExpensePaymentViewHolder>(DiffCallbackPayment) {

    open class ExpensePaymentViewHolder(private val binding: FragmentExpensePaymentBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(payment: Payment) {
            binding.name.text = payment.paymentId.toString()
            binding.total.text = payment.total.toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpensePaymentViewHolder {
        val binding = FragmentExpensePaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpensePaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpensePaymentViewHolder, position: Int) {
        holder.bind(getItem(position))
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