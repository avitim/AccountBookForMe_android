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
import com.example.accountbookforme.adapter.FilterSpinnerAdapter
import com.example.accountbookforme.databinding.DialogAddPaymentBinding
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.model.Payment
import com.example.accountbookforme.viewmodel.ExpenseDetailViewModel
import java.math.BigDecimal

class AddPaymentDialogFragment(
    private val expensePaymentId: Long?,
    private val paymentMethodList: List<Filter>
) : DialogFragment() {

    private lateinit var listener: OnAddedPaymentListener

    private var _binding: DialogAddPaymentBinding? = null
    private val binding get() = _binding!!

    private val expenseDetail: ExpenseDetailViewModel by activityViewModels()

    // 新規作成時に保管する場所
    private var payment = Payment()

    // 結果を渡すリスナー
    interface OnAddedPaymentListener {
        fun addedPayment(payment: Payment)

        fun updatedPayment()
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
        if (expensePaymentId != null) {

            val payment = expenseDetail.getPaymentById(expensePaymentId)
            if (payment != null) {
                val position = paymentMethodList.indexOfFirst { pm ->
                    pm.id == payment.paymentId
                }
                binding.payment.setSelection(position, false)
                binding.paymentTotal.setText(payment.total.toString())
            }
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
                payment.paymentId = paymentMethodList[position].id
            }

            // 選択されなかったとき
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 今のところ特に何もしない
            }
        }

        return AlertDialog.Builder(context)
            .setView(binding.root)
            .setTitle("Enter a payment")
            .setPositiveButton("Add") { _, _ ->
                // 入力内容を反映
                // TODO: いずれは双方向データバインディングで
                if (expensePaymentId == null) {

                    payment.total = BigDecimal(binding.paymentTotal.text.toString())
                    // paymentIdはスピナーで選択時に値更新済みなのでここでは不要

                    // 入力した品物データを渡すリスナー呼び出し
                    listener.addedPayment(payment)

                } else {

                    expenseDetail.setPaymentMethod(expensePaymentId, payment.paymentId)
                    expenseDetail.setPaymentTotal(
                        expensePaymentId,
                        BigDecimal(binding.paymentTotal.text.toString())
                    )

                    // 更新リスナー呼び出し
                    listener.updatedPayment()
                }

            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}