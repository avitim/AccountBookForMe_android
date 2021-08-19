package com.example.accountbookforme.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.accountbookforme.R
import com.example.accountbookforme.databinding.ActivityDetailBinding
import com.example.accountbookforme.fragment.CategoryFragment

class ItemListActivity : AppCompatActivity() {

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

        // Categories画面からのカテゴリIDを受け取る
        val categoryId = intent.extras?.getLong("categoryId")

        // 支出IDをFragmentに渡す
        val bundle = Bundle()
        if (categoryId != null) {
            bundle.putLong("categoryId", categoryId)
        }

        val fragment = CategoryFragment()
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