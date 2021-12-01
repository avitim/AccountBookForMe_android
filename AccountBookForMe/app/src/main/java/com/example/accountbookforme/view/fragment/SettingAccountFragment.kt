package com.example.accountbookforme.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.R
import com.example.accountbookforme.adapter.FilterListAdapter
import com.example.accountbookforme.databinding.FragmentAccountBinding
import com.example.accountbookforme.databinding.FragmentListBinding
import com.example.accountbookforme.repository.AccountRepository
import com.example.accountbookforme.util.RestUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingAccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // アクションバーのタイトルを設定
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.title_account)

        // 非同期スレッドでAPIを叩く
        lifecycleScope.launch {
            val accountRepository: AccountRepository =
                RestUtil.retrofit.create(AccountRepository::class.java)
            val response = accountRepository.findById("1")
            if (response.isSuccessful) {

                val account = response.body()

                if (account != null) {
                    // メインスレッドで画面表示
                    withContext((Dispatchers.Main)) {
                        binding.idValue.text = account.userId
                        binding.nameValue.text = account.name
                        binding.budgetValue.text = account.budget.toString()
                    }
                } else {
                    // 中身が空だったらエラーログを出す
                    Log.e("onViewCreated", "Account is empty!")
                }
            } else {
                Log.e("onViewCreated", response.errorBody().toString())
            }

        }
    }

    fun invoke() {
    }

    /**
     * メニュータップ時の処理設定
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // 前画面に戻る
                findNavController().popBackStack()
            }
        }

        return true
    }
}