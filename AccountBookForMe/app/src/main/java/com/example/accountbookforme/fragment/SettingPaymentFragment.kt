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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.R
import com.example.accountbookforme.adapter.FilterListAdapter
import com.example.accountbookforme.databinding.FragmentListBinding
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.model.Name
import com.example.accountbookforme.viewmodel.PaymentsViewModel

class SettingPaymentFragment : Fragment(), FilterDialogFragment.OnAddFilterListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val paymentsViewModel: PaymentsViewModel by activityViewModels()

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
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.title_payments)

        filterListAdapter.setOnFilterClickListener(
            object : FilterListAdapter.OnFilterClickListener {
                override fun onItemClick(filter: Filter) {
                    FilterDialogFragment(R.string.enter_a_payment, filter).show(
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

        // 決済方法リストの監視開始
        paymentsViewModel.paymentList.observe(viewLifecycleOwner, { paymentList ->
            filterListAdapter.submitList(paymentList)
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
                // 決済方法追加ダイアログ表示
                FilterDialogFragment(R.string.enter_a_payment, null).show(
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
     * 決済方法新規作成
     */
    override fun create(name: String) {
        // 決済方法をDBに新規作成するAPIを投げる
        paymentsViewModel.create(Name(name))
    }

    /**
     * 決済方法更新
     */
    override fun update(filter: Filter) {
        // 決済方法をDB上で更新するAPIを投げる
        paymentsViewModel.update(filter)
        binding.list.adapter?.notifyDataSetChanged()
    }

    /**
     * 決済方法削除
     */
    override fun delete(filter: Filter) {
        // 決済方法をDB上で更新するAPIを投げる
        paymentsViewModel.delete(filter.id!!)
    }
}