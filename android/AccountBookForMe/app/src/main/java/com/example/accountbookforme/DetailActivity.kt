package com.example.accountbookforme

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.accountbookforme.adapter.DialogPaymentsAdapter
import com.example.accountbookforme.model.PaymentListItem
import com.example.accountbookforme.service.PaymentsService
import com.example.accountbookforme.util.RestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class DetailActivity : AppCompatActivity() {

    private var paymentsService: PaymentsService = RestUtil.retrofit.create(PaymentsService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.expense_detail_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_expense)
        // ツールバーをアクションバーの代わりに使う
        setSupportActionBar(toolbar)
        // ツールバーに戻るボタン設置
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 支払方法入力欄タップしたらダイアログが表示されるようにする
        setPaymentListDialog()

        // 日付入力欄の設定
        findViewById<TextView>(R.id.detail_date).setOnClickListener {
            showDatePicker()
        }
    }

    // 戻るボタンの挙動設定
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        // 右上のアイコンのアクション設定
        menuInflater.inflate(R.menu.toolbar_save, menu)
        return true
    }

    private fun setPaymentListDialog() {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.fragment_listview, null)
        val listView = mDialogView.findViewById<ListView>(R.id.container_listView)

        var paymentList: List<PaymentListItem>
        val paymentListCall = paymentsService.getListItems()
        paymentListCall.enqueue( object : Callback<List<PaymentListItem>> {
            override fun onResponse(call: Call<List<PaymentListItem>>?, response: Response<List<PaymentListItem>>?) {

                if(response?.body() != null) {
                    paymentList = response.body()!!

                    listView.adapter = DialogPaymentsAdapter(this@DetailActivity, paymentList)

                    // 支払方法入力欄タップ時の設定
                    findViewById<TextView>(R.id.tmp_detail_method).setOnClickListener {
                        val mBuilder = AlertDialog.Builder(this@DetailActivity)
                            .setView(mDialogView)
                            .setTitle("Select a payment method").create()
                        mBuilder.show()

                        // リストビューのアイテムのクリックイベントを設定
                        listView.setOnItemClickListener { parent, _, position, _ ->

                            // ダイアログを閉じる
                            mBuilder.dismiss()

                            val item = parent.getItemAtPosition(position) as PaymentListItem

                            // 画面の支払方法欄の値を設定
                            findViewById<TextView>(R.id.tmp_payment_method_id).text = item.paymentId.toString()
                            findViewById<TextView>(R.id.tmp_detail_method).text = item.name
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<PaymentListItem>>?, t: Throwable?) {
            }
        })
    }

    private fun showDatePicker() {

        // 今日の日付を取得
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener() { view, year, month, dayOfMonth ->
                findViewById<TextView>(R.id.detail_date).text = formatDate(year, month + 1, dayOfMonth)
            },
            year, month, dayOfMonth
        )
        datePickerDialog.show()
    }

    /**
     * 画面表示用の日付フォーマッタ
     * フォーマットした日付を文字列で返す
     */
    private fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {

        // 日付フォーマッタ
        // 画面表示用
        val dtfToShow = DateTimeFormatter.ofPattern("E, d MMM. yyyy")

        val dateStr = "${year}-${zeroPaddingStr(month)}-${zeroPaddingStr(dayOfMonth)}"
        val dateTime = LocalDate.parse(dateStr)

        return dtfToShow.format(dateTime)
    }

    /**
     * 数字を0埋めした2桁に変換して文字列として返す
     */
    private fun zeroPaddingStr(num: Int): String {
        return num.toString().padStart(2, '0')
    }

}