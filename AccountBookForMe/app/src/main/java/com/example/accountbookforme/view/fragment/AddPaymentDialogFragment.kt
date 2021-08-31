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
import com.example.accountbookforme.MMApplication
import com.example.accountbookforme.R
import com.example.accountbookforme.adapter.FilterSpinnerAdapter
import com.example.accountbookforme.database.entity.ExpensePaymentEntity
import com.example.accountbookforme.databinding.DialogAddPaymentBinding
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.util.Utils
import com.example.accountbookforme.viewmodel.ExpenseDetailsViewModel
import com.example.accountbookforme.viewmodel.ExpenseDetailsViewModelFactory
import java.math.BigDecimal

class AddPaymentDialogFragment(
    private val position: Int?,
    private val ep: ExpensePaymentEntity?,
    private val paymentMethodList: List<Filter>
) : DialogFragment() {

    private lateinit var listener: OnAddedPaymentListener

    private var _binding: DialogAddPaymentBinding? = null
    private val binding get() = _binding!!

    private val expenseDetail: ExpenseDetailsViewModel by activityViewModels {
        ExpenseDetailsViewModelFactory(
            (activity?.application as MMApplication).expenseRepository,
            (activity?.application as MMApplication).itemRepository,
            (activity?.application as MMApplication).epRepository
        )
    }

    // 保存するデータ。既存があれば流用してなければ新規インスタンス生成。
    private var newEp = ep ?: ExpensePaymentEntity()

    // 結果を渡すリスナー
    interface OnAddedPaymentListener {
        fun addPayment(ep: ExpensePaymentEntity)

        fun updatePayment(position: Int, ep: ExpensePaymentEntity)

        fun deletePayment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when {
            context is OnAddedPaymentListener -> listener = context
            parentFragment is OnAddedPaymentListener -> listener =
                parentFragment as OnAddedPaymentListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = DialogAddPaymentBinding.inflate(LayoutInflater.from(context))

        // 決済方法リストのアダプタ設定
        binding.payment.adapter = FilterSpinnerAdapter(requireContext(), paymentMethodList)

        // 前画面から渡された品物データがあれば代入
        if (ep != null) {

            val position = paymentMethodList.indexOfFirst { pm ->
                pm.id == ep.paymentId
            }
            binding.payment.setSelection(position, false)
            binding.paymentTotal.setText(Utils.convertToStrDecimal(ep.total))
        }

        // 決済方法リストのリスナー設定
        binding.payment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            // 選択されたとき
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                newEp.paymentId = paymentMethodList[position].id!!
            }

            // 選択されなかったとき
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 今のところ特に何もしない
            }
        }

        val builder = AlertDialog.Builder(context)
            .setView(binding.root)
            .setTitle("Enter a payment")
            .setPositiveButton("Add", null)
            .setNeutralButton("Cancel", null)


        if (position != null && ep != null) {
            // 更新時は削除ボタンも表示する
            builder.setNegativeButton("Delete") { _, _ ->
                if (ep.id == 0L) {
                    // まだDBには登録されていないのでリストから削除するだけ
                    expenseDetail.removePayment(position)
                } else {
                    // リストから削除して、削除済みリストに登録する
                    expenseDetail.deletePayment(position)
                }

                // 削除リスナー呼び出し
                listener.deletePayment()
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
                if (position == null || ep == null) {
                    // 新規追加

                    newEp.total = BigDecimal(binding.paymentTotal.text.toString())
                    // paymentIdはスピナーで選択時に値更新済みなのでここでは不要

                    // 入力した支払いデータを渡すリスナー呼び出し
                    listener.addPayment(newEp)

                } else {
                    // 更新

                    ep.total = BigDecimal(binding.paymentTotal.text.toString())
                    // paymentIdはスピナーで選択時に値更新済みなのでここでは不要

                    // 入力した支払いデータを渡す更新リスナー呼び出し
                    listener.updatePayment(position, ep)
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

        // 金額が入力されていない
        return if (TextUtils.isEmpty(binding.paymentTotal.text.toString())) {
            // エラートーストを出す
            Toast.makeText(activity, getString(R.string.price_is_empty), Toast.LENGTH_LONG)
                .show()
            false
        } else {
            true
        }
    }
}