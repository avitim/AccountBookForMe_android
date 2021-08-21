package com.example.accountbookforme.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.adapter.FilterListAdapter
import com.example.accountbookforme.databinding.DialogEnterStoreBinding
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.viewmodel.StoresViewModel

class EnterStoreDialogFragment(
    private var id: Long?,
    private var name: String?
) : DialogFragment() {

    private lateinit var listener: OnSelectedStoreListener

    private var _binding: DialogEnterStoreBinding? = null
    private val binding get() = _binding!!

    private val storesViewModel: StoresViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var filterListAdapter: FilterListAdapter

    // 結果を渡すリスナー
    interface OnSelectedStoreListener {
        fun selectedStore(id: Long?, name: String?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when {
            context is OnSelectedStoreListener -> listener = context
            parentFragment is OnSelectedStoreListener -> listener =
                parentFragment as OnSelectedStoreListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = DialogEnterStoreBinding.inflate(LayoutInflater.from(context))

        val mBuilder = AlertDialog.Builder(context)
            .setView(binding.root)
            .setTitle("Enter a store").create()

        if (id == null) {
            binding.enterStoreArea.setText(name)
        }

        // ダイアログのOKボタンのタップイベント
        binding.confirmBtn.setOnClickListener {
            // リストから選択していないので店舗IDはnull
            listener.selectedStore(null, binding.enterStoreArea.text.toString())
            // ダイアログを閉じる
            this.dismiss()
        }

        // 登録済み店舗リスト表示
        recyclerView = binding.storeList
        filterListAdapter = FilterListAdapter()

        storesViewModel.storeList.observe(this, { storeList ->
            filterListAdapter.submitList(storeList)
        })

        // セルのクリック処理
        filterListAdapter.setOnFilterClickListener(
            object : FilterListAdapter.OnFilterClickListener {
                override fun onItemClick(filter: Filter) {

                    // 画面の店舗名欄の値を渡すリスナー呼び出し
                    listener.selectedStore(filter.id, filter.name)

                    // ダイアログの入力欄の値を空にする
                    binding.enterStoreArea.setText("")

                    // ダイアログを閉じる
                    mBuilder.dismiss()
                }
            }
        )

        val linearLayoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = filterListAdapter

        // セルの区切り線表示
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this.context,
                linearLayoutManager.orientation
            )
        )

    return mBuilder
    }
}