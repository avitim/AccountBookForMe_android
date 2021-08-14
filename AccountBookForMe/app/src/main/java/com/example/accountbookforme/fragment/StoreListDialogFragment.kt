package com.example.accountbookforme.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.accountbookforme.R
import com.example.accountbookforme.adapter.DialogStoresAdapter
import com.example.accountbookforme.databinding.DialogStoresBinding
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.repository.StoreRepository
import com.example.accountbookforme.util.RestUtil
import com.example.accountbookforme.viewmodel.ExpensesViewModel
import com.example.accountbookforme.viewmodel.StoreViewModel
import kotlinx.coroutines.launch

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

        val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_stores, null)
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


//        val listView = mDialogView.findViewById<ListView>(R.id.dialog_favorite_store_list)
//
//        val storeListCall = storeRepository.getListItems()
//        storeListCall.enqueue( object : Callback<List<Filter>> {
//            override fun onResponse(call: Call<List<Filter>>?, response: Response<List<Filter>>?) {
//
//                if(response?.body() != null) {
//                    filterList = response.body()!!
//
//                    listView.adapter = DialogStoresAdapter(this@DetailActivity, filterList)
//
//                    // タップした店舗リストのアイテムを値に設定してダイアログを閉じる
//                    listView.setOnItemClickListener { parent, _, position, _ ->
//
//                        val item = parent.getItemAtPosition(position) as Filter
//
//                        // 画面の店舗名欄の値を設定
//                        binding.detailStoreId.text = item.id.toString()
//                        binding.detailStoreName.text = item.name
//
//                        // ダイアログの入力欄の値を空にする
//                        mDialogView.findViewById<EditText>(R.id.dialog_input_store)?.setText("")
//
//                        // ダイアログを閉じる
//                        mBuilder.dismiss()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<List<Filter>>?, t: Throwable?) {
//            }
//        })

        return mBuilder
    }

}