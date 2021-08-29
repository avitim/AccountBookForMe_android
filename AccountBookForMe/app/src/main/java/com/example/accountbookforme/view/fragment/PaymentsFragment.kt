package com.example.accountbookforme.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.adapter.TotalListAdapter
import com.example.accountbookforme.databinding.FragmentListWithMonthBinding
import com.example.accountbookforme.model.Total
import com.example.accountbookforme.util.DateUtil
import com.example.accountbookforme.util.Utils
import com.example.accountbookforme.viewmodel.ExpensesViewModel

class PaymentsFragment : Fragment() {

    private var _binding: FragmentListWithMonthBinding? = null
    private val binding get() = _binding!!

    private val expensesViewModel: ExpensesViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var totalListAdapter: TotalListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListWithMonthBinding.inflate(inflater, container, false)
        val view = binding.root

        // 今月を表示
        binding.month.text = DateUtil.getMonth()

        recyclerView = binding.list
        totalListAdapter = TotalListAdapter()

        // セルのクリック処理
        totalListAdapter.setOnTotalClickListener(
            object : TotalListAdapter.OnTotalClickListener {
                override fun onItemClick(total: Total) {
                    // 今の所何もしない
                }
            }
        )

        val linearLayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = totalListAdapter

        // セルの区切り線表示
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                view.context,
                linearLayoutManager.orientation
            )
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 支出リストの監視開始
        expensesViewModel.totalPaymentList.observe(viewLifecycleOwner, { totalPaymentList ->
            totalListAdapter.setTotalListItems(totalPaymentList)
            // 総額を表示
            binding.allTotal.text = expensesViewModel.totalPaymentList.value?.let {
                Utils.calcTotal(it)
            }?.let { Utils.convertToStrDecimal(it) }
        })
    }
}