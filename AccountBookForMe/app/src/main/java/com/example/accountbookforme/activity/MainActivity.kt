package com.example.accountbookforme.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.accountbookforme.R
import com.example.accountbookforme.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * メインの5つのタブを表示するActivity
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        // 画面下部のナビゲーション
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_expenses,
                R.id.navigation_categories,
                R.id.navigation_payments,
                R.id.navigation_stores,
                R.id.navigation_settings
            )
        )

        val toolbar: Toolbar = binding.mainToolbar
        // ツールバーをアクションバーの代わりに使う
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        // 右上のアイコンの設定
        menuInflater.inflate(R.menu.toolbar_add, menu)
        return true
    }

    // 右上のアイコンクリックイベント
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // 画面下部のナビゲーション情報を取得
        val navController = findNavController(R.id.nav_host_fragment)

        var intent: Intent? = null
        when(navController.currentDestination!!.id) {
            R.id.navigation_expenses -> {
                // 現在のページがExpenseのとき
                intent = Intent(applicationContext, DetailActivity::class.java)
            }
        }
        if (intent != null) {
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }
}