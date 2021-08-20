package com.example.accountbookforme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.databinding.FragmentTotalEachFilterBinding
import com.example.accountbookforme.model.Expense
import com.example.accountbookforme.util.DateUtil

class ExpenseStoreListAdapter : RecyclerView.Adapter<ExpenseStoreListAdapter.ExpenseViewHolder>() {

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

    open class ExpenseViewHolder(binding: FragmentTotalEachFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.name
        val total: TextView = binding.total
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentTotalEachFilterBinding.inflate(layoutInflater, parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {

        val expense = expenseList[position]

        holder.name.text = DateUtil.formatDate(expense.purchasedAt, DateUtil.DATE_YYYYMMDD)
        holder.total.text = "¥" + expense.total

        // セルのクリックイベントにリスナーをセット
        holder.itemView.setOnClickListener {
            listener.onItemClick(expense)
        }
    }

    // リストのサイズ取得
    override fun getItemCount() = expenseList.size

    // カテゴリごとの支出額リストをセットして変更を通知
    fun setExpenseStoreListItems(expenseList: List<Expense>) {
        this.expenseList = expenseList
        notifyDataSetChanged()
    }
}