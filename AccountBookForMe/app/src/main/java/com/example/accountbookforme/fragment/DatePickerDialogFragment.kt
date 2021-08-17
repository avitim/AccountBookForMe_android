package com.example.accountbookforme.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.example.accountbookforme.util.DateUtil
import java.util.Calendar

class DatePickerDialogFragment(private val dateTime: String?) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    private lateinit var listener: OnSelectedDateListener

    // 結果を渡すリスナー
    interface OnSelectedDateListener {
        fun selectedDate(year: Int, month: Int, day: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when {
            context is OnSelectedDateListener -> listener = context
            parentFragment is OnSelectedDateListener -> listener =
                parentFragment as OnSelectedDateListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // 今日の日付取得
        val calendar = Calendar.getInstance()

        if (dateTime != null) {
            // 日付指定があればそれを初期値に設定する
            calendar.time = DateUtil.convertStringToCalender(dateTime)
        }

        val context = context

        return when {
            context != null -> {
                DatePickerDialog(
                    context,
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            }
            else -> super.onCreateDialog(savedInstanceState)
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener.selectedDate(year, month, dayOfMonth)
    }
}