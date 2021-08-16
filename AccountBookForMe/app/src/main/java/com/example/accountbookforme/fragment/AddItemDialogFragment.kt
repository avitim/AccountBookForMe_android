package com.example.accountbookforme.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.accountbookforme.adapter.DialogItemCategoryAdapter
import com.example.accountbookforme.databinding.DialogAddItemBinding
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.model.Item
import com.example.accountbookforme.viewmodel.ExpenseDetailViewModel
import java.math.BigDecimal

class AddItemDialogFragment(private val itemId: Long?, private val categoryList: List<Filter>) : DialogFragment() {

    private lateinit var listener: OnAddedItemListener

    private var _binding: DialogAddItemBinding? = null
    private val binding get() = _binding!!

    private val expenseDetail: ExpenseDetailViewModel by activityViewModels()

    // 新規作成時に保管する場所
    private var item = Item()

    // 結果を渡すリスナー
    interface OnAddedItemListener {
        fun addedItem(item: Item)

        fun updatedItem()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when {
            context is OnAddedItemListener -> listener = context
            parentFragment is OnAddedItemListener -> listener = parentFragment as OnAddedItemListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = DialogAddItemBinding.inflate(LayoutInflater.from(context))

        // 前画面から渡された品物データがあれば代入
        if (itemId != null) {

            val item = expenseDetail.getItemById(itemId)
            if (item != null) {
                binding.itemName.setText(item.name)
                binding.itemPrice.setText(item.price.toString())
            }
        }

        // カテゴリリスト作成
        binding.itemCategory.adapter = DialogItemCategoryAdapter(requireContext(), categoryList)
        binding.itemCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            // 選択されたとき
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (itemId == null) {
                    item.categoryId = categoryList[position].id
                } else {
                    expenseDetail.getItemById(itemId)?.categoryId = categoryList[position].id
                }
            }

            // 選択されなかったとき
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 今のところ特に何もしない
            }
        }

        return AlertDialog.Builder(context)
            .setView(binding.root)
            .setTitle("Enter an item")
            .setPositiveButton("Add") { _, _ ->
                // 入力内容を反映
                // TODO: いずれは双方向データバインディングで
                if (itemId == null) {

                    item.name = binding.itemName.text.toString()
                    item.price = BigDecimal(binding.itemPrice.text.toString())
                    // 入力した品物データを渡すリスナー呼び出し
                    listener.addedItem(item)

                } else {

                    expenseDetail.setItemName(itemId, binding.itemName.text.toString())
                    expenseDetail.setItemPrice(itemId, BigDecimal(binding.itemPrice.text.toString()))
                    // categoryIdはスピナーで選択時に値更新済みなのでここでは不要
                    // 更新リスナー呼び出し
                    listener.updatedItem()
                }

            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}