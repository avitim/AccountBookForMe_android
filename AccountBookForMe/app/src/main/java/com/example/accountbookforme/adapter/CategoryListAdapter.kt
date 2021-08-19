package com.example.accountbookforme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.databinding.FragmentTotalEachFilterBinding
import com.example.accountbookforme.model.TotalEachFilter

class CategoryListAdapter : RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder>() {

    private lateinit var listener: OnCategoryClickListener
    private var categoryList: List<TotalEachFilter> = listOf()

    // 結果を渡すリスナー
    interface OnCategoryClickListener {
        fun onItemClick(category: TotalEachFilter)
    }

    // リスナーをセット
    fun setOnCategoryClickListener(listener: OnCategoryClickListener) {
        this.listener = listener
    }

    open class CategoryViewHolder(binding: FragmentTotalEachFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.name
        val total: TextView = binding.total
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentTotalEachFilterBinding.inflate(layoutInflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        val category = categoryList[position]

        holder.name.text = category.name
        holder.total.text = "¥" + category.total

        // セルのクリックイベントにリスナーをセット
        holder.itemView.setOnClickListener {
            listener.onItemClick(category)
        }
    }

    // リストのサイズ取得
    override fun getItemCount() = categoryList.size

    // カテゴリごとの支出額リストをセットして変更を通知
    fun setCategoryListItems(categoryList: List<TotalEachFilter>) {
        this.categoryList = categoryList
        notifyDataSetChanged()
    }
}