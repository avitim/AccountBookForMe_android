package com.example.accountbookforme.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.accountbookforme.R
import com.example.accountbookforme.databinding.DialogFilterBinding
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.model.Name

class FilterDialogFragment(
    private val title: Int,
    private val filter: Filter?
) : DialogFragment() {

    private lateinit var listener: OnAddFilterListener

    private var _binding: DialogFilterBinding? = null
    private val binding get() = _binding!!

    // 結果を渡すリスナー
    interface OnAddFilterListener {
        fun create(name: Name)

        fun update(filter: Filter)

        fun delete(filter: Filter)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when {
            context is OnAddFilterListener -> listener = context
            parentFragment is OnAddFilterListener -> listener =
                parentFragment as OnAddFilterListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = DialogFilterBinding.inflate(LayoutInflater.from(context))

        // 前画面から渡されたデータがあれば代入
        if (filter != null) {
            binding.editFilterName.setText(filter.name)
        }

        val builder = AlertDialog.Builder(context)
            .setView(binding.root)
            .setTitle(title)
            .setPositiveButton(R.string.label_save) { _, _ ->
                // 入力内容を反映
                // TODO: いずれは双方向データバインディングで

                if (filter == null) {
                    // 新規作成
                    listener.create(Name(binding.editFilterName.text.toString()))
                } else {
                    // 更新
                    filter.name = binding.editFilterName.text.toString()
                    listener.update(filter)
                }
            }
            .setNeutralButton(R.string.label_cancel, null)

        if (filter != null) {
            // 編集時は削除ボタンも表示する
            builder.setNegativeButton(R.string.label_delete) { _, _ ->
                // TODO: 関連する支出や品物がある場合は消せないようにする
                // 削除
                listener.delete(filter)
            }
        }

        return builder.create()
    }
}