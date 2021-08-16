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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.accountbookforme.R
import com.example.accountbookforme.adapter.ExpenseItemListAdapter
import com.example.accountbookforme.adapter.ExpensePaymentListAdapter
import com.example.accountbookforme.databinding.FragmentExpenseDetailBinding
import com.example.accountbookforme.model.Item
import com.example.accountbookforme.model.Payment
import com.example.accountbookforme.util.DateUtil
import com.example.accountbookforme.viewmodel.CategoryViewModel
import com.example.accountbookforme.viewmodel.ExpenseDetailViewModel
import com.example.accountbookforme.viewmodel.PaymentViewModel
import com.example.accountbookforme.viewmodel.StoreViewModel
import java.math.BigDecimal

class ExpenseDetailFragment : Fragment(),
    DatePickerDialogFragment.OnSelectedDateListener,
    EnterStoreDialogFragment.OnSelectedStoreListener,
    AddItemDialogFragment.OnAddedItemListener,
    AddPaymentDialogFragment.OnAddedPaymentListener {

    private var _binding: FragmentExpenseDetailBinding? = null
    private val binding get() = _binding!!

    private val expenseDetail: ExpenseDetailViewModel by activityViewModels()
    private val storeViewModel: StoreViewModel by activityViewModels()
    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val paymentViewModel: PaymentViewModel by activityViewModels()

    private lateinit var itemListAdapter: ExpenseItemListAdapter
    private lateinit var paymentListAdapter: ExpensePaymentListAdapter

    private var id: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        _binding = FragmentExpenseDetailBinding.inflate(inflater, container, false)

        itemListAdapter = ExpenseItemListAdapter(categoryViewModel)
        paymentListAdapter = ExpensePaymentListAdapter(paymentViewModel)

        // 各ViewModelに値をセットする
        storeViewModel.getStoreList()
        categoryViewModel.getCategoryList()
        paymentViewModel.getPaymentList()

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
                object : ExpenseItemListAdapter.OnExpenseItemClickListener {
                    override fun onItemClick(item: Item) {
                        AddItemDialogFragment(item.id, categoryViewModel.categoryList.value!!).show(
                            childFragmentManager,
                            null
                        )
                    }
                }
            )
            binding.itemList.adapter = itemListAdapter
            val itemLinearLayoutManager = LinearLayoutManager(view.context)
            binding.itemList.layoutManager = itemLinearLayoutManager
            binding.itemList.addItemDecoration(
                DividerItemDecoration(
                    view.context,
                    itemLinearLayoutManager.orientation
                )
            )

            // 支払いリストをセットする
            // クリックイベントを設定
            paymentListAdapter.setOnExpensePaymentClickListener(
                object : ExpensePaymentListAdapter.OnExpensePaymentClickListener {
                    override fun onItemClick(payment: Payment) {
                        AddPaymentDialogFragment(
                            payment.id,
                            paymentViewModel.paymentList.value!!
                        ).show(childFragmentManager, null)
                    }
                }
            )
            binding.paymentList.adapter = paymentListAdapter
            val paymentLinearLayoutManager = LinearLayoutManager(view.context)
            binding.paymentList.layoutManager = paymentLinearLayoutManager
            binding.paymentList.addItemDecoration(
                DividerItemDecoration(
                    view.context,
                    paymentLinearLayoutManager.orientation
                )
            )
        }

        // 支出詳細の監視開始
        expenseDetail.expenseDetail.observe(viewLifecycleOwner, { expenseDetail ->
            itemListAdapter.submitList(expenseDetail.itemList)
            paymentListAdapter.submitList(expenseDetail.paymentList)

            // 品物の合計額表示
            updateItemTotal()

            // 支払いの合計額表示
            updatePaymentTotal()
        })

        // 日付入力欄をタップしたらカレンダーダイアログを表示する
        binding.purchasedAt.setOnClickListener {
            DatePickerDialogFragment().show(childFragmentManager, null)
        }

        // 店舗リストアイコンをタップしたら店舗リストダイアログを表示する
        binding.storeList.setOnClickListener {
            EnterStoreDialogFragment(
                expenseDetail.expenseDetail.value?.storeId,
                expenseDetail.expenseDetail.value?.storeName,
                storeViewModel.storeList.value!!
            ).show(childFragmentManager, null)
        }

        // 品物追加アイコンをタップしたら品物追加ダイアログを表示する
        binding.addItemBtn.setOnClickListener {
            AddItemDialogFragment(null, categoryViewModel.categoryList.value!!).show(
                childFragmentManager,
                null
            )
        }

        // 支払い追加アイコンをタップしたら支払い追加ダイアログを表示する
        binding.addPaymentBtn.setOnClickListener {
            AddPaymentDialogFragment(null, paymentViewModel.paymentList.value!!).show(
                childFragmentManager,
                null
            )
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

    // 店舗を入力したときに呼ばれる from EnterStoreDialogFragment
    override fun selectedStore(id: Long?, name: String?) {
        binding.storeName.text = name
        expenseDetail.expenseDetail.value?.storeId = id
        expenseDetail.expenseDetail.value?.storeName = name
    }

    // 品物を入力したときに呼ばれる from AddItemDialogFragment
    override fun addedItem(item: Item) {
        expenseDetail.addItem(item)
        // 品物の合計額更新
        updateItemTotal()
    }

    // 品物を更新したときに呼ばれる from AddItemDialogFragment
    override fun updatedItem() {
        binding.itemList.adapter?.notifyDataSetChanged()
        // 品物の合計額更新
        updateItemTotal()
    }

    // 支払いを入力したときに呼ばれる from AddPaymentDialogFragment
    override fun addedPayment(payment: Payment) {
        expenseDetail.addPayment(payment)
        // 支払いの合計額更新
        updatePaymentTotal()
    }

    // 支払いを更新したときに呼ばれる from AddPaymentDialogFragment
    override fun updatedPayment() {
        binding.paymentList.adapter?.notifyDataSetChanged()
        // 支払いの合計額更新
        updatePaymentTotal()
    }

    // 品物の合計額を計算して表示
    private fun updateItemTotal() {

        val total = expenseDetail.getItemList()?.fold(BigDecimal.ZERO) { acc, item ->
            acc + item.price
        }
        binding.numTotalItem.text = total.toString()
    }

    // 支払いの合計額を計算して表示
    private fun updatePaymentTotal() {

        val total = expenseDetail.getPaymentList()?.fold(BigDecimal.ZERO) { acc, payment ->
            acc + payment.total
        }
        binding.numTotalPayment.text = total.toString()
    }

}