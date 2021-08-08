package com.example.accountbookforme.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.activity.DetailActivity
import com.example.accountbookforme.adapter.ExpenseAdapter
import com.example.accountbookforme.databinding.FragmentExpensesBinding
import com.example.accountbookforme.model.Expense
import com.example.accountbookforme.repository.ExpenseRepository
import com.example.accountbookforme.viewmodel.ExpensesViewModel
import com.example.accountbookforme.util.RestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class ExpensesFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(ExpensesViewModel::class.java) }

    private var _binding: FragmentExpensesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter

    private var expenseRepository: ExpenseRepository = RestUtil.retrofit.create(ExpenseRepository::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExpensesBinding.inflate(inflater, container, false)
        val view = binding.root

        // 今月を表示する処理
        getMonth()

        recyclerView = binding.expenseList
        expenseAdapter = ExpenseAdapter(this.requireContext())

        // セルのクリック処理
        expenseAdapter.setOnExpenseItemClickListener(
            object : ExpenseAdapter.OnExpenseItemClickListener {
                override fun onItemClick(expense: Expense) {
                    val intent = Intent(context, DetailActivity::class.java)
                    // 支出IDを渡す
                    intent.putExtra("expenseId", expense.expenseId)
                    // 支出詳細画面に遷移する
                    startActivity(intent)
                }
            }
        )

        val linearLayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = expenseAdapter

        // セルの区切り線表示
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, linearLayoutManager.orientation))

        // 一覧に表示するデータを非同期で取得
        val expenseList = expenseRepository.getAllItems()
        expenseList.enqueue( object : Callback<List<Expense>> {
            override fun onResponse(call: Call<List<Expense>>?, response: Response<List<Expense>>?) {

                if(response?.body() != null) {
                    expenseAdapter.setExpenseListItems(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Expense>>?, t: Throwable?) {
                Log.e("expenseFragment", "Something is wrong: " + t)
            }
        })

        return view
    }

    private fun getMonth() {
        val nowDate: LocalDateTime = LocalDateTime.now()
        binding.month.text = nowDate.month.toString()
    }
}