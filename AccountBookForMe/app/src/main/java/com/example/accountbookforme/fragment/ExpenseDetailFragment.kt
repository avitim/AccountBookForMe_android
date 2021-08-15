package com.example.accountbookforme.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.accountbookforme.R
import com.example.accountbookforme.adapter.ExpenseItemAdapter
import com.example.accountbookforme.adapter.ExpensePaymentAdapter
import com.example.accountbookforme.databinding.ExpenseDetailFragmentBinding
import com.example.accountbookforme.model.Item
import com.example.accountbookforme.util.DateUtil
import com.example.accountbookforme.viewmodel.CategoryViewModel
import com.example.accountbookforme.viewmodel.ExpenseDetailViewModel
import com.example.accountbookforme.viewmodel.StoreViewModel

class ExpenseDetailFragment : Fragment(),
    DatePickerDialogFragment.OnSelectedDateListener,
    StoreListDialogFragment.OnSelectedStoreListener,
    DialogAddItem.OnAddedItemListener{

    private var _binding: ExpenseDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val expenseDetail: ExpenseDetailViewModel by activityViewModels()
    private lateinit var storeViewModel: StoreViewModel
    private lateinit var categoryViewModel: CategoryViewModel

    private val itemListAdapter = ExpenseItemAdapter()
    private val paymentListAdapter = ExpensePaymentAdapter()

    private var id: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ExpenseDetailFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        storeViewModel = ViewModelProvider(this).get(StoreViewModel::class.java)
        storeViewModel.getStoreList()
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        categoryViewModel.getCategoryList()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = expenseDetail

        // 前画面から渡された支出IDを取得
        val bundle = arguments
        if (bundle != null) {
            id = bundle.getLong("id")
        }

        if (id != null) {
            // 支出詳細取得
            expenseDetail.getExpenseDetail(id!!)

            // アイテムリストをセットする
            // クリックイベントを設定
            itemListAdapter.setOnExpenseItemClickListener(
                object : ExpenseItemAdapter.OnExpenseItemClickListener {
                    override fun onItemClick(item: Item) {
                        DialogAddItem(item.id, categoryViewModel.categoryList.value!!).show(childFragmentManager, null)
                    }
                }
            )
            binding.itemList.adapter = itemListAdapter
            val itemLinearLayoutManager = LinearLayoutManager(view.context)
            binding.itemList.layoutManager = itemLinearLayoutManager
            binding.itemList.addItemDecoration(DividerItemDecoration(view.context, itemLinearLayoutManager.orientation))

            // 支払いリストをセットする
            val paymentLinearLayoutManager = LinearLayoutManager(view.context)
            binding.paymentList.adapter = paymentListAdapter
            binding.paymentList.layoutManager = paymentLinearLayoutManager
            binding.paymentList.addItemDecoration(DividerItemDecoration(view.context, paymentLinearLayoutManager.orientation))

        }

        // 支出詳細の監視開始
        expenseDetail.expenseDetail.observe(viewLifecycleOwner, { expenseDetail ->
            itemListAdapter.submitList(expenseDetail.itemList)
            paymentListAdapter.submitList(expenseDetail.paymentList)
        })

        // 日付入力欄をタップしたらカレンダーダイアログを表示する
        binding.purchasedAt.setOnClickListener {
            DatePickerDialogFragment().show(childFragmentManager, null)
        }

        // 店舗リストアイコンをタップしたら店舗リストダイアログを表示する
        binding.storeList.setOnClickListener {
            StoreListDialogFragment(
                expenseDetail.expenseDetail.value?.storeId,
                expenseDetail.expenseDetail.value?.storeName,
                storeViewModel.storeList.value!!
            ).show(childFragmentManager, null)
        }

        // 品物追加アイコンをタップしたら品物追加ダイアログを表示する
        binding.addItemBtn.setOnClickListener {
            DialogAddItem(null, categoryViewModel.categoryList.value!!).show(childFragmentManager, null)
        }

    }

    // メニュー表示
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_save, menu)
    }

    // メニュータップ時の処理設定
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                // 支出IDがnullなら新規作成API、それ以外なら更新APIを投げる
                Toast.makeText(context, "Save!", Toast.LENGTH_LONG).show()
            }
            android.R.id.home -> {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        return true
    }

    // 日付ダイアログで選択したときに呼ばれる from DatePickerDialogFragment
    override fun selectedDate(year: Int, month: Int, day: Int) {
        val dateTime = DateUtil.parseLocalDateTimeFromInt(year, month, day)
        binding.purchasedAt.text = DateUtil.formatDate(dateTime, DateUtil.DATE_EDMMMYYYY)
        expenseDetail.expenseDetail.value?.purchasedAt = dateTime
    }

    // 店舗を入力したときに呼ばれる from StoreListDialogFragment
    override fun selectedStore(id: Long?, name: String?) {
        binding.storeName.text = name
        expenseDetail.expenseDetail.value?.storeId = id
        expenseDetail.expenseDetail.value?.storeName = name
    }

    // 品物を入力したときに呼ばれる from DialogAddItem
    override fun addedItem(item: Item) {
        expenseDetail.addItem(item)
    }

    // 品物を更新したときに呼ばれる from DialogAddItem
    override fun updatedItem() {
        binding.itemList.adapter?.notifyDataSetChanged()
    }
}