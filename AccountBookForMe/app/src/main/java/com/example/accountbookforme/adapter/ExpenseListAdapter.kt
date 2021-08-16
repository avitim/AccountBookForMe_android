package com.example.accountbookforme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.databinding.FragmentExpenseBinding
import com.example.accountbookforme.model.Expense
import com.example.accountbookforme.util.DateUtil

class ExpenseListAdapter : RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder>() {

    private lateinit var listener: OnExpenseClickListener
    private var expenseList: List<Expense> = listOf()

    // 結果を渡すリスナー
    interface OnExpenseClickListener {
        fun onItemClick(expense: Expense)
    }

    // リスナーをセット
    fun setOnExpenseClickListener(listener: OnExpenseClickListener) {
        this.listener = listener
    }

    open class ExpenseViewHolder(binding: FragmentExpenseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val purchasedAt: TextView = binding.purchasedAt
        val storeName: TextView = binding.storeName
        val total: TextView = binding.total
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentExpenseBinding.inflate(layoutInflater, parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {

        val expenseItem = expenseList[position]

        holder.purchasedAt.text =
            DateUtil.formatDate(expenseItem.purchasedAt, DateUtil.DATE_YYYYMMDD)
        holder.total.text = "¥" + expenseItem.total
        holder.storeName.text = expenseItem.storeName

        // セルのクリックイベントにリスナーをセット
        holder.itemView.setOnClickListener {
            listener.onItemClick(expenseItem)
        }
    }

    // リストのサイズ取得
    override fun getItemCount() = expenseList.size

    // 支出リストセットして変更を通知
    fun setExpenseListItems(expenseList: List<Expense>) {
        this.expenseList = expenseList
        notifyDataSetChanged()
    }
}