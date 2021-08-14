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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.accountbookforme.R
import com.example.accountbookforme.adapter.DialogPaymentsAdapter
import com.example.accountbookforme.adapter.DialogStoresAdapter
import com.example.accountbookforme.databinding.ExpenseDetailActivityBinding
import com.example.accountbookforme.fragment.ExpenseDetailFragment
import com.example.accountbookforme.model.ExpenseDetail
import com.example.accountbookforme.model.Payment
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.repository.ExpenseRepository
import com.example.accountbookforme.repository.PaymentRepository
import com.example.accountbookforme.repository.StoreRepository
import com.example.accountbookforme.util.DateUtil
import com.example.accountbookforme.util.RestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

// TODO: 肥大化しかかっているのでFragmentに分けたい
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ExpenseDetailActivityBinding

    private var paymentRepository: PaymentRepository = RestUtil.retrofit.create(PaymentRepository::class.java)
    private var expenseRepository: ExpenseRepository = RestUtil.retrofit.create(ExpenseRepository::class.java)
    private var storeRepository: StoreRepository = RestUtil.retrofit.create(StoreRepository::class.java)

    private var expenseId: Long? = null
    private lateinit var paymentList: List<Payment>
    private lateinit var filterList: List<Filter>

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

        // 支出IDをFragmentに渡す
        val bundle = Bundle()
        bundle.putLong("id", expenseId!!)

        val fragment = ExpenseDetailFragment()
        fragment.arguments = bundle

        // Fragment生成
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment, fragment)
                .commit()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        // 戻るボタンをタップしたら前の画面に遷移する
        onBackPressedDispatcher.onBackPressed()
        return true
    }


}