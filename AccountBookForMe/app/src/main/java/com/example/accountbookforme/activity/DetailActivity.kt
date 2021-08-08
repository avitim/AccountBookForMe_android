package com.example.accountbookforme.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.accountbookforme.R
import com.example.accountbookforme.adapter.DialogPaymentsAdapter
import com.example.accountbookforme.adapter.DialogStoresAdapter
import com.example.accountbookforme.databinding.ExpenseDetailActivityBinding
import com.example.accountbookforme.model.Expense
import com.example.accountbookforme.model.ExpenseDetail
import com.example.accountbookforme.model.PaymentListItem
import com.example.accountbookforme.model.Store
import com.example.accountbookforme.repository.ExpenseRepository
import com.example.accountbookforme.repository.PaymentRepository
import com.example.accountbookforme.repository.StoreRepository
import com.example.accountbookforme.util.DateUtil
import com.example.accountbookforme.util.RestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.Calendar

// TODO: 肥大化しかかっているのでFragmentに分けたい
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ExpenseDetailActivityBinding

    private var paymentRepository: PaymentRepository = RestUtil.retrofit.create(PaymentRepository::class.java)
    private var expenseRepository: ExpenseRepository = RestUtil.retrofit.create(ExpenseRepository::class.java)
    private var storeRepository: StoreRepository = RestUtil.retrofit.create(StoreRepository::class.java)

    private var expenseId: Long? = null
    private lateinit var paymentList: List<PaymentListItem>
    private lateinit var storeList: List<Store>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ExpenseDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbarExpense
        // ツールバーをアクションバーの代わりに使う
        setSupportActionBar(toolbar)
        // ツールバーに戻るボタン設置
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 今日の日付を取得
        val calendar = Calendar.getInstance()
        val todayYear = calendar.get(Calendar.YEAR)
        // calenderのmonthは0始まりなので1足して実際の月に合わせる
        val todayMonth = calendar.get(Calendar.MONTH) + 1
        val todayDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Expenses画面からの支出IDを受け取る
        expenseId = intent.extras?.getLong("expenseId")

        if (expenseId != null) {
            // 支出IDがnull以外の場合、そのIDをもとに表示する値を非同期で取得する
            expenseRepository.getDetailById(expenseId!!).enqueue( object : Callback<ExpenseDetail> {
                override fun onResponse(call: Call<ExpenseDetail>?, response: Response<ExpenseDetail>?) {
                    val expenseDetail = response?.body()!!
                    val expense = expenseDetail.expense

                    // 値を各ビューにセットする
                    binding.detailTotalAmountValue.text = expense.totalAmount.toString()
                    expenseDetail.paymentMethods.forEach { payment ->
                        binding.detailPaymentMethodId.text = payment.id.toString()
                        binding.detailPaymentMethod.text = payment.name
                        binding.detailSubTotal.setText(payment.subTotal.toString())
                    }
                    binding.detailStoreId.text = if (expense.storeId == null) "" else expense.storeId.toString()
                    binding.detailStoreName.text = expense.storeName
                    binding.detailNote.setText(if (expense.note == null) "" else expense.note)
                    setPurchaseDate(LocalDate.parse(expense.purchasedAt))

                    // 登録済みの店舗の一覧を非同期で取得し、店舗名入力欄タップしたらダイアログを表示するように設定
                    setStoreListDialog()
                }

                override fun onFailure(call: Call<ExpenseDetail>?, t: Throwable?) {
                }
            })

            // 削除用確認ダイアログ生成
            val builder = AlertDialog.Builder(this)
                .setMessage("Delete item?")
                .setPositiveButton("OK") { _, _ ->
                    // OKをタップしたらデータを削除する
                    expenseRepository.delete(expenseId!!).enqueue(object : Callback<Long> {
                        override fun onResponse(call: Call<Long>?, response: Response<Long>?) {
                            // 前の画面に遷移する
                            // TODO: Expenses画面表示用のデータを再取得してほしいのでfinish()は使わないが、Observeで実装すれば不要になりそう
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
            binding.deleteExpense.setOnClickListener {
                builder.show()
            }
        }
        else {
            // 新規作成の場合

            // 購入日のデフォルトを今日にする
            setPurchaseDate(DateUtil.parseLocalDateFromInt(todayYear, todayMonth, todayDayOfMonth))

            // 削除ボタンを非表示にする
            binding.deleteExpense.visibility = View.GONE
        }

        // 支払方法の一覧を非同期で取得し、支払方法入力欄タップしたらダイアログを表示するように設定
        setPaymentListDialog()

        // 日付入力欄をタップしたらカレンダーを表示する
        binding.detailDate.setOnClickListener {
            showDatePicker(todayYear, todayMonth - 1, todayDayOfMonth)
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

                val subTotalStr = binding.detailSubTotal.text.toString()
                val subTotal = subTotalStr.toFloatOrNull()
                val methodId = binding.detailPaymentMethodId.text.toString().toLong()
                val purchasedAt = binding.detailFullDate.text.toString()
                val storeId = binding.detailStoreId.text.toString().toLongOrNull()
                // storeIdがnullではない場合はstoreNameの値は不要なのでnullにする
                val storeName = if (storeId != null) null else binding.detailStoreName.text.toString()
                val note = binding.detailNote.text.toString()

                val expense = Expense(subTotal!!, purchasedAt, storeId, storeName, note)
                val payments = mutableListOf<PaymentListItem>()
                payments.add(PaymentListItem(methodId, "", subTotal))
                val expenseDetail = ExpenseDetail(expense, payments)

                // 入力した値をDBに保存する
                val call: Call<Expense> = if (expenseId == null) {
                    // 新規作成
                    expenseRepository.create(expenseDetail)
                } else {
                    // 更新
                    expenseRepository.update(expenseId!!, expenseDetail)
                }
                call.enqueue( object : Callback<Expense> {
                    override fun onResponse(call: Call<Expense>?, response: Response<Expense>?) {
                        // うまく行けばExpenses画面に遷移する
                        // TODO: Expenses画面表示用のデータを再取得してほしいのでfinish()は使わないが、Observeで実装すれば不要になりそう
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

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_payments, null)
        val listView = mDialogView.findViewById<ListView>(R.id.dialog_payment_list)

        val paymentListCall = paymentRepository.getListItems()
        paymentListCall.enqueue( object : Callback<List<PaymentListItem>> {
            override fun onResponse(call: Call<List<PaymentListItem>>?, response: Response<List<PaymentListItem>>?) {

                if(response?.body() != null) {
                    paymentList = response.body()!!

                    listView.adapter = DialogPaymentsAdapter(this@DetailActivity, paymentList)
                    val mBuilder = AlertDialog.Builder(this@DetailActivity)
                        .setView(mDialogView)
                        .setTitle("Select a payment method").create()

                    // 支払方法欄をタップしたら支払方法選択ダイアログを表示する
                    binding.detailPaymentMethod.setOnClickListener {
                        mBuilder.show()

                        // タップした支払方法リストのアイテムを値に設定してダイアログを閉じる
                        listView.setOnItemClickListener { parent, _, position, _ ->

                            val item = parent.getItemAtPosition(position) as PaymentListItem

                            // 画面の支払方法欄の値を設定
                            binding.detailPaymentMethodId.text = item.id.toString()
                            binding.detailPaymentMethod.text = item.name

                            // ダイアログを閉じる
                            mBuilder.dismiss()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<PaymentListItem>>?, t: Throwable?) {
            }
        })
    }

    private fun setStoreListDialog() {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_stores, null)
        val mBuilder = AlertDialog.Builder(this@DetailActivity)
            .setView(mDialogView)
            .setTitle("Enter a store").create()

        // 店舗名欄のタップイベント
        val storeName = binding.detailStoreName
        storeName.setOnClickListener {

            // 店舗IDがnullなら店舗名欄の値を入力欄に入れる
            if (findViewById<TextView>(R.id.detail_store_id).text.isEmpty()) {
                val textStr = storeName.text.toString()
                mDialogView.findViewById<EditText>(R.id.dialog_input_store)?.setText(textStr)
            }

            // ダイアログを表示
            mBuilder.show()
        }

        // ダイアログのOKボタンのタップイベント
        mDialogView.findViewById<Button>(R.id.dialog_input_confirm).setOnClickListener {
            // 画面の店舗欄の値を設定、リストから選択しない場合の店舗IDは空
            binding.detailStoreId.text = ""
            storeName.text = mDialogView.findViewById<EditText>(R.id.dialog_input_store).text

            // ダイアログを閉じる
            mBuilder.dismiss()
        }

        val listView = mDialogView.findViewById<ListView>(R.id.dialog_favorite_store_list)

        val storeListCall = storeRepository.getListItems()
        storeListCall.enqueue( object : Callback<List<Store>> {
            override fun onResponse(call: Call<List<Store>>?, response: Response<List<Store>>?) {

                if(response?.body() != null) {
                    storeList = response.body()!!

                    listView.adapter = DialogStoresAdapter(this@DetailActivity, storeList)

                    // タップした店舗リストのアイテムを値に設定してダイアログを閉じる
                    listView.setOnItemClickListener { parent, _, position, _ ->

                        val item = parent.getItemAtPosition(position) as Store

                        // 画面の店舗名欄の値を設定
                        binding.detailStoreId.text = item.id.toString()
                        binding.detailStoreName.text = item.name

                        // ダイアログの入力欄の値を空にする
                        mDialogView.findViewById<EditText>(R.id.dialog_input_store)?.setText("")

                        // ダイアログを閉じる
                        mBuilder.dismiss()
                    }
                }
            }

            override fun onFailure(call: Call<List<Store>>?, t: Throwable?) {
            }
        })
    }

    private fun showDatePicker(default_year: Int, default_month: Int, default_dayOfMonth: Int) {

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                setPurchaseDate(DateUtil.parseLocalDateFromInt(year, month + 1, dayOfMonth))
            },
            default_year, default_month, default_dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun setPurchaseDate(localDate: LocalDate) {
        binding.detailDate.text = DateUtil.formatDate(localDate, DateUtil.DATE_EDMMMYYYY)
        binding.detailFullDate.text = DateUtil.formatDate(localDate, DateUtil.DATE_YYYYMMDD)
    }

}