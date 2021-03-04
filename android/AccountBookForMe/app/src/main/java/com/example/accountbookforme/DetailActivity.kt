package com.example.accountbookforme

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.accountbookforme.adapter.DialogPaymentsAdapter
import com.example.accountbookforme.model.Expense
import com.example.accountbookforme.model.ExpenseDetail
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

    private var expenseId: Long? = null
    private lateinit var paymentList: List<PaymentListItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.expense_detail_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_expense)
        // ツールバーをアクションバーの代わりに使う
        setSupportActionBar(toolbar)
        // ツールバーに戻るボタン設置
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 支払方法の一覧を非同期で取得し、支払方法入力欄タップしたらダイアログを表示するように設定
        setPaymentListDialog()

        // Expenses画面からの支出IDを受け取る
        expenseId = intent.extras?.getLong("expenseId")

        if (expenseId != null) {
            // 支出IDがnull以外の場合、そのIDをもとに表示する値を非同期で取得する
            expenseService.getDetailById(expenseId!!).enqueue( object : Callback<ExpenseDetail> {
                override fun onResponse(call: Call<ExpenseDetail>?, response: Response<ExpenseDetail>?) {
                    val expenseDetail = response?.body()!!
                    val expense = expenseDetail.expense

                    // 値を各ビューにセットする
                    findViewById<TextView>(R.id.detail_total_amount_value).text = expense.totalAmount.toString()
                    expenseDetail.paymentMethods.forEach { payment ->
                        findViewById<TextView>(R.id.detail_payment_method_id).text = payment.key.toString()
                        // paymentIdは固有なので要素は1つしかヒットしない想定
                        findViewById<TextView>(R.id.detail_payment_method).text = paymentList.filter { method ->
                            method.paymentId == payment.key
                        }[0].name
                        findViewById<EditText>(R.id.detail_sub_payment_amount).setText(if (payment.value == null) "" else payment.value.toString())
                    }
                    findViewById<TextView>(R.id.detail_date).text = expense.purchasedAt
                    // TODO: 店舗がIDで設定されている場合は店舗リストからIDで検索してきて名前をセットする
                    findViewById<EditText>(R.id.detail_store_name).setText(if (expense.storeName == null) "ぬる" else expense.storeName)
                    findViewById<EditText>(R.id.detail_note).setText(if (expense.note == null) "" else expense.note)

                }

                override fun onFailure(call: Call<ExpenseDetail>?, t: Throwable?) {
                }
            })

            // 削除用確認ダイアログ生成
            val builder = AlertDialog.Builder(this)
                .setMessage("Delete item?")
                .setPositiveButton("OK") { _, _ ->
                    // OKをタップしたらデータを削除する
                    expenseService.delete(expenseId!!).enqueue(object : Callback<Long> {
                        override fun onResponse(call: Call<Long>?, response: Response<Long>?) {
                            // 前の画面に遷移する
                            // TODO: EXpenses画面表示用のデータを再取得してほしいのでfinish()は使わないが、Observeで実装すれば不要になりそう
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                        }

                        override fun onFailure(call: Call<Long>?, t: Throwable?) {
                        }
                    })
                }
                .setNegativeButton("Cancel") { _, _ ->
                    // Cancelをタップしたらなにもしない
                }
                .create()

            // 削除ボタンをタップしたら確認ダイアログを表示する
            findViewById<TextView>(R.id.delete_expense).setOnClickListener {
                builder.show()
            }
        }
        else {
            // 新規作成の場合は削除ボタンを非表示にする
            findViewById<TextView>(R.id.delete_expense).visibility = View.GONE
        }

        // 今日の日付を取得
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // 日付入力欄のデフォルトを今日にする
        setPurchaseDate(year, month + 1, dayOfMonth)

        // 日付入力欄をタップしたらカレンダーを表示する
        findViewById<TextView>(R.id.detail_date).setOnClickListener {
            showDatePicker(year, month, dayOfMonth)
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

                val totalAmountStr = findViewById<EditText>(R.id.detail_sub_payment_amount).text.toString()
                val totalAmount = totalAmountStr.toFloatOrNull()
                val methodId = findViewById<TextView>(R.id.detail_payment_method_id).text.toString().toLong()
                val purchasedAt = findViewById<TextView>(R.id.detail_full_date).text.toString()
                val storeName = findViewById<EditText>(R.id.detail_store_name).text.toString()
                val note = findViewById<EditText>(R.id.detail_note).text.toString()

                val expense = Expense(totalAmount!!, purchasedAt, null, storeName, note)
                val payments = HashMap<Long, Float>()
                payments[methodId] = totalAmount
                val expenseDetail = ExpenseDetail(expense, payments)

                // 入力した値をDBに保存する
                val call: Call<Expense> = if (expenseId == null) {
                    // 新規作成
                    expenseService.create(expenseDetail)
                } else {
                    // 更新
                    expenseService.update(expenseId!!, expenseDetail)
                }
                call.enqueue( object : Callback<Expense> {
                    override fun onResponse(call: Call<Expense>?, response: Response<Expense>?) {
                        // うまく行けばExpenses画面に遷移する
                        // TODO: EXpenses画面表示用のデータを再取得してほしいのでfinish()は使わないが、Observeで実装すれば不要になりそう
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
                    findViewById<TextView>(R.id.detail_payment_method).setOnClickListener {
                        mBuilder.show()

                        // タップした支払方法リストのアイテムを値に設定してダイアログを閉じる
                        listView.setOnItemClickListener { parent, _, position, _ ->

                            // ダイアログを閉じる
                            mBuilder.dismiss()

                            val item = parent.getItemAtPosition(position) as PaymentListItem

                            // 画面の支払方法欄の値を設定
                            findViewById<TextView>(R.id.detail_payment_method_id).text = item.paymentId.toString()
                            findViewById<TextView>(R.id.detail_payment_method).text = item.name
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<PaymentListItem>>?, t: Throwable?) {
            }
        })
    }

    private fun showDatePicker(year: Int, month: Int, dayOfMonth: Int) {

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, seletedDayOfMonth ->
                setPurchaseDate(selectedYear, selectedMonth + 1, seletedDayOfMonth)
            },
            year, month, dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun setPurchaseDate(year: Int, month: Int, dayOfMonth: Int) {
        findViewById<TextView>(R.id.detail_date).text = formatDate(year, month, dayOfMonth, "E, d MMM. yyyy")
        findViewById<TextView>(R.id.detail_full_date).text = formatDate(year, month, dayOfMonth, "yyyy-MM-dd")
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