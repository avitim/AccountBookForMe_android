package com.example.accountbookforme.view.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.accountbookforme.R
import com.example.accountbookforme.adapter.FilterSpinnerAdapter
import com.example.accountbookforme.databinding.DialogAddItemBinding
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.model.Item
import com.example.accountbookforme.util.Utils
import com.example.accountbookforme.viewmodel.ExpenseDetailViewModel
import java.math.BigDecimal

class AddItemDialogFragment(
    private val position: Int?,
    private val item: Item?,
    private val categoryList: List<Filter>
) :
    DialogFragment() {

    private lateinit var listener: OnAddedItemListener

    private var _binding: DialogAddItemBinding? = null
    private val binding get() = _binding!!

    private val expenseDetailViewModel: ExpenseDetailViewModel by activityViewModels()

    // 保存するデータ。既存があれば流用してなければ新規インスタンス生成。
    private var newItem = item ?: Item()

    // 結果を渡すリスナー
    interface OnAddedItemListener {
        fun addItem(item: Item)

        fun updateItem()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when {
            context is OnAddedItemListener -> listener = context
            parentFragment is OnAddedItemListener -> listener =
                parentFragment as OnAddedItemListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = DialogAddItemBinding.inflate(LayoutInflater.from(context))

        // カテゴリリストのアダプタ設定
        binding.itemCategory.adapter = FilterSpinnerAdapter(requireContext(), categoryList)

        // 前画面から渡された品物データがあれば代入
        if (item != null) {

            val position = categoryList.indexOfFirst { category ->
                category.id == item.categoryId
            }
            binding.itemCategory.setSelection(position, false)
            binding.itemName.setText(item.name)
            binding.itemPrice.setText(Utils.convertToStrDecimal(item.price))
        }

        // カテゴリリストのリスナー設定
        binding.itemCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            // 選択されたとき
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                newItem.categoryId = categoryList[position].id!!
            }

            // 選択されなかったとき
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 今のところ特に何もしない
            }
        }

        val builder = AlertDialog.Builder(context)
            .setView(binding.root)
            .setTitle("Enter an item")
            .setPositiveButton("Add", null)
            .setNeutralButton("Cancel", null)

        if (position != null && item != null) {
            // 更新時は削除ボタンも表示する
            builder.setNegativeButton("Delete") { _, _ ->
                if (item.id == null) {
                    // まだDBには登録されていないのでリストから削除するだけ
                    expenseDetailViewModel.removeItem(position)
                } else {
                    // リストから削除して、削除済みリストに登録する
                    expenseDetailViewModel.deleteItem(position)
                }

                // 更新リスナー呼び出し
                listener.updateItem()
            }
        }

        val dialog = builder.create()
        dialog.show()
        // OKボタンタップ時の処理
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            // バリデーションチェック
            if (validationCheck()) {
                // 成功

                // 入力内容を反映
                // TODO: いずれは双方向データバインディングで
                if (position == null || item == null) {
                    // 新規追加

                    newItem.name = binding.itemName.text.toString()
                    newItem.price = BigDecimal(binding.itemPrice.text.toString())
                    // categoryIdはスピナーで選択時に値更新済みなのでここでは不要

                    // 入力した品物データを渡すリスナー呼び出し
                    listener.addItem(newItem)

                } else {
                    // 更新

                    expenseDetailViewModel.setItemName(
                        position,
                        binding.itemName.text.toString()
                    )
                    expenseDetailViewModel.setItemPrice(
                        position,
                        BigDecimal(binding.itemPrice.text.toString())
                    )
                    expenseDetailViewModel.setItemCategory(position, item.categoryId)

                    // 更新リスナー呼び出し
                    listener.updateItem()
                }
                // ダイアログを閉じる
                dialog.dismiss()
            }
        }

        return dialog
    }

    /**
     * バリデーションチェック
     */
    private fun validationCheck(): Boolean {

        var isValidated = true
        var message = ""

        // 品名が入力されていない
        if (TextUtils.isEmpty(binding.itemName.text.toString())) {
            isValidated = false
            message += getString(R.string.name_is_empty)
        }
        // 金額が入力されていない
        if (TextUtils.isEmpty(binding.itemPrice.text.toString())) {
            isValidated = false
            message += getString(R.string.price_is_empty)
        }

        // バリデーションチェックがfalseならエラートーストを出す
        if (!isValidated) {
            Toast.makeText(activity, message, Toast.LENGTH_LONG)
                .show()
        }
        return isValidated
    }
}