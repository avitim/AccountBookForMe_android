package com.example.accountbookforme.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.R
import com.example.accountbookforme.model.Expense

class ExpensesAdapter(val context: Context) : RecyclerView.Adapter<ExpensesAdapter.ExpenseItemViewHolder>() {

    var expenseList: List<Expense> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseItemViewHolder {

        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_expenses_item, parent, false)
        return ExpenseItemViewHolder(item)
    }

    override fun onBindViewHolder(holder: ExpenseItemViewHolder, position: Int) {

        val expenseItem = expenseList[position]

        holder.storeName.text = expenseItem.storeId.toString()
        holder.firstItem.text = expenseItem.purchasedAt.toString()
        holder.price.text = expenseItem.totalAmount.toString()
        holder.paymentMethod.text = expenseItem.note
    }

    open class ExpenseItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val storeName: TextView = view.findViewById(R.id.store_name)
        val firstItem: TextView = view.findViewById(R.id.first_item)
        val price: TextView = view.findViewById(R.id.price)
        val paymentMethod: TextView = view.findViewById(R.id.payment_method)
    }

    // リストのサイズ取得
    override fun getItemCount() = expenseList.size

    fun setExpenseListItems(expenseList: List<Expense>) {
        this.expenseList = expenseList
        notifyDataSetChanged()
    }
}