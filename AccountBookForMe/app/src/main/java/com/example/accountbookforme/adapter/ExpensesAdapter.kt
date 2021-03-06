package com.example.accountbookforme.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.R
import com.example.accountbookforme.model.ExpenseListItem

class ExpensesAdapter(val context: Context) : RecyclerView.Adapter<ExpensesAdapter.ExpenseItemViewHolder>() {

    private var expenseList: List<ExpenseListItem> = listOf()
    private lateinit var listener: OnExpenseItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseItemViewHolder {

        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_expenses_item, parent, false)
        return ExpenseItemViewHolder(item)
    }

    override fun onBindViewHolder(holder: ExpenseItemViewHolder, position: Int) {

        val expenseItem = expenseList[position]

        val priceText = "¥" + expenseItem.price.toString()

        holder.purchasedAt.text = expenseItem.purchasedAt
        holder.price.text = priceText
        holder.paymentMethod.text = expenseItem.method
        holder.storeName.text = expenseItem.store

        // セルのクリックイベントにリスナーをセット
        holder.itemView.setOnClickListener {
            listener.onItemClick(expenseItem)
        }
    }

    // インターフェースを作成
    interface OnExpenseItemClickListener {
        fun onItemClick(expenseListItem: ExpenseListItem)
    }

    // リスナーをセット
    fun setOnExpenseItemClickListener(listener: OnExpenseItemClickListener) {
        this.listener = listener
    }

    open class ExpenseItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val purchasedAt: TextView = view.findViewById(R.id.purchased_at)
        val storeName: TextView = view.findViewById(R.id.store_name)
        val price: TextView = view.findViewById(R.id.price)
        val paymentMethod: TextView = view.findViewById(R.id.payment_method)
    }

    // リストのサイズ取得
    override fun getItemCount() = expenseList.size

    fun setExpenseListItems(expenseList: List<ExpenseListItem>) {
        this.expenseList = expenseList
        notifyDataSetChanged()
    }
}