package com.example.accountbookforme.fragment

import android.content.Intent
import android.os.Bundle
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
import com.example.accountbookforme.viewmodel.ExpensesViewModel
import java.time.LocalDateTime

class ExpensesFragment : Fragment() {

    private var _binding: FragmentExpensesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpensesBinding.inflate(inflater, container, false)
        val view = binding.root

        // 今月を表示する処理
        getMonth()

        recyclerView = binding.expenseList
        expenseAdapter = ExpenseAdapter()

        // セルのクリック処理
        expenseAdapter.setOnExpenseClickListener(
            object : ExpenseAdapter.OnExpenseClickListener {
                override fun onItemClick(expense: Expense) {
                    val intent = Intent(context, DetailActivity::class.java)
                    // 支出IDを渡す
                    intent.putExtra("expenseId", expense.id)
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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 支出リストの監視開始
        val expensesViewModel = ViewModelProvider(this).get(ExpensesViewModel::class.java)
        expensesViewModel.expenseList.observe(viewLifecycleOwner, { expenseList ->
            expenseAdapter.setExpenseListItems(expenseList)
        })
    }

    // 今月を取得してビューにセット
    private fun getMonth() {
        val nowDate: LocalDateTime = LocalDateTime.now()
        binding.month.text = nowDate.month.toString()
    }
}