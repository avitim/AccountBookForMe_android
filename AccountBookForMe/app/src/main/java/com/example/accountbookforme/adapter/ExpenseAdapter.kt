package com.example.accountbookforme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.R
import com.example.accountbookforme.model.Expense
import com.example.accountbookforme.util.DateUtil

class ExpenseAdapter : RecyclerView.Adapter<ExpenseAdapter.ExpenseItemViewHolder>() {

    private var expenseList: List<Expense> = listOf()
    private lateinit var listener: OnExpenseItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseItemViewHolder {

        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_expenses_item, parent, false)
        return ExpenseItemViewHolder(item)
    }

    override fun onBindViewHolder(holder: ExpenseItemViewHolder, position: Int) {

        val expenseItem = expenseList[position]

        holder.purchasedAt.text = DateUtil.formatDate(expenseItem.purchasedAt, DateUtil.DATE_YYYYMMDD)
        holder.total.text = "¥" + expenseItem.total
        holder.storeName.text = expenseItem.storeName

        // セルのクリックイベントにリスナーをセット
        holder.itemView.setOnClickListener {
            listener.onItemClick(expenseItem)
        }
    }

    // インターフェースを作成
    interface OnExpenseItemClickListener {
        fun onItemClick(expense: Expense)
    }

    // リスナーをセット
    fun setOnExpenseItemClickListener(listener: OnExpenseItemClickListener) {
        this.listener = listener
    }

    open class ExpenseItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val purchasedAt: TextView = view.findViewById(R.id.purchased_at)
        val storeName: TextView = view.findViewById(R.id.store_name)
        val total: TextView = view.findViewById(R.id.total)
    }

    // リストのサイズ取得
    override fun getItemCount() = expenseList.size

    fun setExpenseListItems(expenseList: List<Expense>) {
        this.expenseList = expenseList
        notifyDataSetChanged()
    }
}