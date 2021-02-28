package com.example.accountbookforme.ui.expenses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.R
import com.example.accountbookforme.adapter.ExpensesAdapter
import com.example.accountbookforme.model.ExpenseListItem
import com.example.accountbookforme.repository.ExpenseRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class ExpensesFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(ExpensesViewModel::class.java) }

    private lateinit var recyclerView: RecyclerView
    private lateinit var expensesAdapter: ExpensesAdapter

    private val expenseRepository = ExpenseRepository.instance

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = layoutInflater.inflate(R.layout.fragment_expenses, container, false)

        getMonth(view)

        recyclerView = view.findViewById(R.id.expense_list)
        expensesAdapter = ExpensesAdapter(this.requireContext())

        val linearLayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = expensesAdapter

        // セルの区切り線表示
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, linearLayoutManager.orientation))

        val expenseList = expenseRepository.getAll()
        expenseList.enqueue( object : Callback<List<ExpenseListItem>> {
            override fun onResponse(call: Call<List<ExpenseListItem>>?, response: Response<List<ExpenseListItem>>?) {

                if(response?.body() != null) {
                    expensesAdapter.setExpenseListItems(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<ExpenseListItem>>?, t: Throwable?) {

            }
        })

        return view
    }

    private fun getMonth(view: View) {
        val nowDate: LocalDateTime = LocalDateTime.now()
        view.findViewById<TextView>(R.id.month).text = nowDate.month.toString()
    }
}