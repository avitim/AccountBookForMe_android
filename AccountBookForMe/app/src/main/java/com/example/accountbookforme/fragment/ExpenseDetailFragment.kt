package com.example.accountbookforme.fragment

import android.app.AlertDialog
import android.content.Intent
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
import com.example.accountbookforme.activity.MainActivity
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
            id = bundle.get("id") as Long?
        }

        if (id == null) {
            // 新規作成時

            // 空の支出詳細生成
            expenseDetail.createExpenseDetail()

            // 削除ボタンを非表示にする
            binding.deleteExpense.visibility = View.GONE

        } else {
            // 既存の支出詳細表示時

            // 支出詳細取得
            expenseDetail.getExpenseDetail(id!!)

        }

        // アイテムリストをセットする
        // クリックイベントを設定
        itemListAdapter.setOnExpenseItemClickListener(
            object : ExpenseItemListAdapter.OnExpenseItemClickListener {
                override fun onItemClick(position: Int, item: Item) {
                    AddItemDialogFragment(
                        position,
                        item,
                        categoryViewModel.categoryList.value!!
                    ).show(
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
                override fun onItemClick(position: Int, payment: Payment) {
                    AddPaymentDialogFragment(
                        position,
                        payment,
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
            DatePickerDialogFragment(expenseDetail.getPurchasedAt()).show(
                childFragmentManager,
                null
            )
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
            AddItemDialogFragment(null, null, categoryViewModel.categoryList.value!!).show(
                childFragmentManager,
                null
            )
        }

        // 支払い追加アイコンをタップしたら支払い追加ダイアログを表示する
        binding.addPaymentBtn.setOnClickListener {
            AddPaymentDialogFragment(null, null, paymentViewModel.paymentList.value!!).show(
                childFragmentManager,
                null
            )
        }

        // 削除ボタンをタップしたら確認ダイアログを表示する
        binding.deleteExpense.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("Delete this expense?")
                .setPositiveButton("OK") { _, _ ->
                    // 支出詳細をDB上から削除するAPIを投げる
                    expenseDetail.delete().observe(viewLifecycleOwner, { isSuccessful ->
                        if (isSuccessful) {
                            // 成功したら支出一覧画面に遷移する
                            startActivity(Intent(context, MainActivity::class.java))
                        } else {
                            // 失敗したらとりあえずエラートーストを出しておく
                            // TODO: 正式な対処は今後実装する
                            Toast.makeText(activity, "Something is wrong!", Toast.LENGTH_LONG)
                                .show()
                        }
                    })
                }
                .setNegativeButton("Cancel") { _, _ ->
                    // なにもしない
                }
                .show()
        }

    }

    // メニュー表示
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
    }

    // メニュータップ時の処理設定
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                // 支出IDがnullなら新規作成API、それ以外なら更新APIを投げる
                if (id == null) {
                    // 支出詳細をDB上で新規作成するAPIを投げる
                    expenseDetail.create().observe(viewLifecycleOwner, { isSuccessful ->
                        if (isSuccessful) {
                            // 成功したら支出一覧画面に遷移する
                            startActivity(Intent(context, MainActivity::class.java))
                        } else {
                            // 失敗したらとりあえずエラートーストを出しておく
                            // TODO: 正式な対処は今後実装する
                            Toast.makeText(activity, "Something is wrong!", Toast.LENGTH_LONG)
                                .show()
                        }
                    })
                } else {
                    // 支出詳細をDB上で更新するAPIを投げる
                    expenseDetail.update().observe(viewLifecycleOwner, { isSuccessful ->
                        if (isSuccessful) {
                            // 成功したら支出一覧画面に遷移する
                            startActivity(Intent(context, MainActivity::class.java))
                        } else {
                            // 失敗したらとりあえずエラートーストを出しておく
                            // TODO: 正式な対処は今後実装する
                            Toast.makeText(activity, "Something is wrong!", Toast.LENGTH_LONG)
                                .show()
                        }
                    })
                }
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