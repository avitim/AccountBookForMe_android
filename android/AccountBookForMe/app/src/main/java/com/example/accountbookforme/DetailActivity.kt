package com.example.accountbookforme

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.accountbookforme.adapter.DialogPaymentsAdapter
import com.example.accountbookforme.model.Expense
import com.example.accountbookforme.model.ExpenseForm
import com.example.accountbookforme.model.PaymentListItem
import com.example.accountbookforme.service.ExpenseService
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
    private var expenseService: ExpenseService = RestUtil.retrofit.create(ExpenseService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.expense_detail_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_expense)
        // ツールバーをアクションバーの代わりに使う
        setSupportActionBar(toolbar)
        // ツールバーに戻るボタン設置
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 支払方法入力欄タップしたらダイアログを表示する
        setPaymentListDialog()

        // 日付入力欄をタップしたらカレンダーを表示する
        findViewById<TextView>(R.id.detail_date).setOnClickListener {
            showDatePicker()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // 戻るボタンをタップしたら前の画面に遷移する
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // 画面右上メニューのリソースを保存ボタンに設定
        menuInflater.inflate(R.menu.toolbar_save, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId) {
            // 保存ボタン（画面右上）をタップした場合
            R.id.menu_save -> {

                val totalAmountStr = findViewById<EditText>(R.id.tmp_total_amount_value).text.toString()
                val totalAmount = totalAmountStr.toFloatOrNull()
                val purchasedAt = findViewById<TextView>(R.id.detail_purchased_date_str).text.toString()
                val storeName = findViewById<EditText>(R.id.detail_store_name).text.toString()
                val note = findViewById<EditText>(R.id.detail_note).text.toString()

                val expense = Expense(totalAmount!!, purchasedAt, null, storeName, note)
                val paymentMethods = arrayListOf(findViewById<TextView>(R.id.tmp_payment_method_id).text.toString().toLong())
                val expenseForm = ExpenseForm(expense, paymentMethods)

                // 入力した値をDBに保存する
                expenseService.create(expenseForm).enqueue( object : Callback<Expense> {
                    override fun onResponse(call: Call<Expense>?, response: Response<Expense>?) {
                        // うまく行けばExpenses画面に遷移する
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onFailure(call: Call<Expense>?, t: Throwable?) {
                    }
                })

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
                    val mBuilder = AlertDialog.Builder(this@DetailActivity)
                        .setView(mDialogView)
                        .setTitle("Select a payment method").create()

                    // 支払方法入力欄をタップしたら支払方法選択ダイアログを表示する
                    findViewById<TextView>(R.id.tmp_detail_method).setOnClickListener {
                        mBuilder.show()

                        // タップした支払方法リストのアイテムを値に設定してダイアログを閉じる
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
                findViewById<TextView>(R.id.detail_date).text = formatDate(year, month + 1, dayOfMonth, "E, d MMM. yyyy")
                findViewById<TextView>(R.id.detail_purchased_date_str).text = formatDate(year, month + 1, dayOfMonth, "yyyy-MM-dd")
            },
            year, month, dayOfMonth
        )
        datePickerDialog.show()
    }

    /**
     * 画面表示用の日付フォーマッタ
     * フォーマットした日付を文字列で返す
     */
    private fun formatDate(year: Int, month: Int, dayOfMonth: Int, pattern: String): String {

        // 日付フォーマッタ
        // 画面表示用
        val dtfToShow = DateTimeFormatter.ofPattern(pattern)

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