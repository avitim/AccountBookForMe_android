package com.example.accountbookforme

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
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * メインの5つのタブを表示するActivity
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

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

        val toolbar: Toolbar = findViewById(R.id.main_toolbar)
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