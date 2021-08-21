package com.example.accountbookforme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.databinding.SimpleListItemBinding
import com.example.accountbookforme.model.Item

class ItemListAdapter : RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>() {

    private lateinit var listener: OnItemClickListener
    private var itemList: List<Item> = listOf()

    // 結果を渡すリスナー
    interface OnItemClickListener {
        fun onItemClick(item: Item)
    }

    // リスナーをセット
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    open class ItemViewHolder(binding: SimpleListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.label
        val total: TextView = binding.value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SimpleListItemBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = itemList[position]

        holder.name.text = item.name
        holder.total.text = "¥" + item.price

        // セルのクリックイベントにリスナーをセット
        holder.itemView.setOnClickListener {
            listener.onItemClick(item)
        }
    }

    // リストのサイズ取得
    override fun getItemCount() = itemList.size

    // カテゴリごとの支出額リストをセットして変更を通知
    fun setItemListItems(itemList: List<Item>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }
}