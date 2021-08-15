package com.example.accountbookforme.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.accountbookforme.adapter.DialogStoresAdapter
import com.example.accountbookforme.databinding.DialogStoresBinding
import com.example.accountbookforme.model.Filter

class StoreListDialogFragment(private var id: Long?, private var name: String?, private val storeList: List<Filter>) : DialogFragment() {

    private var _binding: DialogStoresBinding? = null
    private val binding get() = _binding!!

    // 結果を渡すリスナー
    interface OnSelectedStoreListener {
        fun selectedStore(id: Long?, name: String?)
    }

    private lateinit var listener: OnSelectedStoreListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when {
            context is OnSelectedStoreListener -> listener = context
            parentFragment is OnSelectedStoreListener -> listener = parentFragment as OnSelectedStoreListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = DialogStoresBinding.inflate(LayoutInflater.from(context))

        val mBuilder = AlertDialog.Builder(context)
            .setView(binding.root)
            .setTitle("Enter a store").create()

        if (id == null) {
            binding.dialogInputStore.setText(name)
        }

        // ダイアログのOKボタンのタップイベント
        binding.dialogInputConfirm.setOnClickListener {
            // リストから選択していないので店舗IDはnull
            listener.selectedStore(null, binding.dialogInputStore.text.toString())
            // ダイアログを閉じる
            this.dismiss()
        }

        // 登録済み店舗リスト表示
        binding.dialogFavoriteStoreList.adapter = DialogStoresAdapter(requireContext(), storeList)

        // タップした店舗リストのアイテムを返却してダイアログを閉じる
        binding.dialogFavoriteStoreList.setOnItemClickListener { parent, _, position, _ ->

            val store = parent.getItemAtPosition(position) as Filter

            // 画面の店舗名欄の値を渡すリスナー呼び出し
            listener.selectedStore(store.id, store.name)

            // ダイアログの入力欄の値を空にする
            binding.dialogInputStore.setText("")

            // ダイアログを閉じる
            mBuilder.dismiss()
        }

        return mBuilder
    }

}