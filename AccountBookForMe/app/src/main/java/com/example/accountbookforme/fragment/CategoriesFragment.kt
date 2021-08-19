package com.example.accountbookforme.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.activity.ItemListActivity
import com.example.accountbookforme.adapter.CategoryListAdapter
import com.example.accountbookforme.databinding.FragmentListWithMonthBinding
import com.example.accountbookforme.model.TotalEachFilter
import com.example.accountbookforme.util.DateUtil
import com.example.accountbookforme.viewmodel.CategoriesViewModel

class CategoriesFragment : Fragment() {

    private var _binding: FragmentListWithMonthBinding? = null
    private val binding get() = _binding!!

    private val categoriesViewModel: CategoriesViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryListAdapter: CategoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListWithMonthBinding.inflate(inflater, container, false)
        val view = binding.root

        // 今月を表示
        binding.month.text = DateUtil.getMonth()

        recyclerView = binding.expenseList
        categoryListAdapter = CategoryListAdapter()

        // セルのクリック処理
        categoryListAdapter.setOnCategoryClickListener(
            object : CategoryListAdapter.OnCategoryClickListener {
                override fun onItemClick(category: TotalEachFilter) {
                    val intent = Intent(context, ItemListActivity::class.java)
                    // 支出IDを渡す
                    intent.putExtra("categoryId", category.id)
                    // 支出詳細画面に遷移する
                    startActivity(intent)
                }
            }
        )

        val linearLayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = categoryListAdapter

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
        categoriesViewModel.totalList.observe(viewLifecycleOwner, { totalList ->
            categoryListAdapter.setCategoryListItems(totalList)
            // 総額を表示
            binding.allTotal.text = categoriesViewModel.calcTotal().toString()
        })
    }
}