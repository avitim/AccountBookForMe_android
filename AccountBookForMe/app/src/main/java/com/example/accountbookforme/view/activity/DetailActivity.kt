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

        // Expenses画面からの値を受け取る
        val expenseId = intent.extras?.getLong("expenseId")
        val storeName = intent.extras?.getString("storeName")

        // 値をFragmentに渡す
        val bundle = Bundle()
        expenseId?.let { bundle.putLong("id", it) }
        storeName?.let { bundle.putString("storeName", it) }

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