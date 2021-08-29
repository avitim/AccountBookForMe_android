package com.example.accountbookforme.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.accountbookforme.R
import com.example.accountbookforme.databinding.ActivityDetailBinding
import com.example.accountbookforme.view.fragment.ExpenseDetailFragment

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbarExpense
        // ツールバーをアクションバーの代わりに使う
        setSupportActionBar(toolbar)
        // ツールバーに戻るボタン設置
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Expenses画面からの支出IDを受け取る
        val expenseId = intent.extras?.getLong("expenseId")

        // 支出IDをFragmentに渡す
        val bundle = Bundle()
        if (expenseId != null) {
            bundle.putLong("id", expenseId)
        }

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