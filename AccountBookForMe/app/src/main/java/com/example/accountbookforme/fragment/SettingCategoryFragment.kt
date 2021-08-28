package com.example.accountbookforme.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.R
import com.example.accountbookforme.adapter.FilterListAdapter
import com.example.accountbookforme.application.MMApplication
import com.example.accountbookforme.databinding.FragmentListBinding
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.viewmodel.CategoriesViewModel
import com.example.accountbookforme.viewmodel.CategoriesViewModelFactory

class SettingCategoryFragment : Fragment(), FilterDialogFragment.OnAddFilterListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val categoriesViewModel: CategoriesViewModel by viewModels {
        CategoriesViewModelFactory((activity?.application as MMApplication).categoryRepository)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var filterListAdapter: FilterListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root

        recyclerView = binding.list
        filterListAdapter = FilterListAdapter()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // アクションバーのタイトルを設定
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.title_categories)

        filterListAdapter.setOnFilterClickListener(
            object : FilterListAdapter.OnFilterClickListener {
                override fun onItemClick(filter: Filter) {
                    FilterDialogFragment(R.string.enter_a_category, filter).show(
                        childFragmentManager,
                        null
                    )
                }
            }
        )

        val linearLayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = filterListAdapter

        // セルの区切り線表示
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                view.context,
                linearLayoutManager.orientation
            )
        )

        // カテゴリリストの監視開始
        categoriesViewModel.categoryList.observe(viewLifecycleOwner, {
            filterListAdapter.submitList(categoriesViewModel.getCategoriesAsFilter())
        })
    }

    /**
     * メニュー表示
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
    }

    /**
     * メニュータップ時の処理設定
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                // カテゴリ追加ダイアログ表示
                FilterDialogFragment(R.string.enter_a_category, null).show(
                    childFragmentManager,
                    null
                )
            }
            android.R.id.home -> {
                // 前画面に戻る
                findNavController().popBackStack()
            }
        }

        return true
    }

    /**
     * カテゴリ新規作成
     */
    override fun create(name: String) {
        // カテゴリをDBに新規作成するAPIを投げる
        categoriesViewModel.create(name)
    }

    /**
     * カテゴリ更新
     */
    override fun update(filter: Filter) {
        // カテゴリをDB上で更新するAPIを投げる
        categoriesViewModel.update(filter)
        binding.list.adapter?.notifyDataSetChanged()
    }

    /**
     * カテゴリ削除
     */
    override fun delete(filter: Filter) {
        // カテゴリをDB上で更新するAPIを投げる
        categoriesViewModel.deleteById(filter.id!!)
    }
}