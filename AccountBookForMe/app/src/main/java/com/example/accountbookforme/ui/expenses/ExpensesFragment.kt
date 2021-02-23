package com.example.accountbookforme.ui.expenses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.accountbookforme.R
import java.time.LocalDateTime

class ExpensesFragment : Fragment() {

    private lateinit var expensesViewModel: ExpensesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        expensesViewModel =
            ViewModelProvider(this).get(ExpensesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_expenses, container, false)
        val textView: TextView = root.findViewById(R.id.text_expenses)
        expensesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        getMonth(root)

        return root
    }

    private fun getMonth(view : View) {
        val nowDate: LocalDateTime = LocalDateTime.now()

        val month = view.findViewById<TextView>(R.id.month)
        month.text = nowDate.month.toString()
    }
}