package com.example.accountbookforme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.databinding.SimpleListItemBinding
import com.example.accountbookforme.model.Total
import com.example.accountbookforme.util.TextUtil

class TotalListAdapter : RecyclerView.Adapter<TotalListAdapter.TotalViewHolder>() {

    private lateinit var listener: OnTotalClickListener
    private var totalList: List<Total> = listOf()

    // 結果を渡すリスナー
    interface OnTotalClickListener {
        fun onItemClick(total: Total)
    }

    // リスナーをセット
    fun setOnTotalClickListener(listener: OnTotalClickListener) {
        this.listener = listener
    }

    open class TotalViewHolder(binding: SimpleListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.label
        val total: TextView = binding.value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TotalViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SimpleListItemBinding.inflate(layoutInflater, parent, false)
        return TotalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TotalViewHolder, position: Int) {

        val category = totalList[position]

        holder.name.text = category.name
        holder.total.text = TextUtil.convertToStr(category.total)

        // セルのクリックイベントにリスナーをセット
        holder.itemView.setOnClickListener {
            listener.onItemClick(category)
        }
    }

    // リストのサイズ取得
    override fun getItemCount() = totalList.size

    // カテゴリごとの支出額リストをセットして変更を通知
    fun setTotalListItems(totalList: List<Total>) {
        this.totalList = totalList
        notifyDataSetChanged()
    }
}