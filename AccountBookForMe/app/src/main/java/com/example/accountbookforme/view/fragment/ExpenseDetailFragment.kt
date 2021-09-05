package com.example.accountbookforme.view.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.accountbookforme.MMApplication
import com.example.accountbookforme.R
import com.example.accountbookforme.adapter.ExpenseItemListAdapter
import com.example.accountbookforme.adapter.ExpensePaymentListAdapter
import com.example.accountbookforme.database.entity.ExpensePaymentEntity
import com.example.accountbookforme.database.entity.ItemEntity
import com.example.accountbookforme.databinding.FragmentExpenseDetailBinding
import com.example.accountbookforme.util.DateUtil
import com.example.accountbookforme.util.Utils
import com.example.accountbookforme.view.activity.MainActivity
import com.example.accountbookforme.viewmodel.CategoriesViewModel
import com.example.accountbookforme.viewmodel.CategoriesViewModelFactory
import com.example.accountbookforme.viewmodel.ExpenseDetailsViewModel
import com.example.accountbookforme.viewmodel.ExpenseDetailsViewModelFactory
import com.example.accountbookforme.viewmodel.PaymentsViewModel
import com.example.accountbookforme.viewmodel.PaymentsViewModelFactory
import com.example.accountbookforme.viewmodel.StoresViewModel
import com.example.accountbookforme.viewmodel.StoresViewModelFactory
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ExpenseDetailFragment : Fragment(),
    DatePickerDialogFragment.OnSelectedDateListener,
    EnterStoreDialogFragment.OnSelectedStoreListener,
    AddItemDialogFragment.OnAddedItemListener,
    AddPaymentDialogFragment.OnAddedPaymentListener {

    private var _binding: FragmentExpenseDetailBinding? = null
    private val binding get() = _binding!!

    private val expenseDetail: ExpenseDetailsViewModel by activityViewModels {
        ExpenseDetailsViewModelFactory(
            (activity?.application as MMApplication).expenseRepository,
            (activity?.application as MMApplication).itemRepository,
            (activity?.application as MMApplication).epRepository
        )
    }
    private val storesViewModel: StoresViewModel by activityViewModels {
        StoresViewModelFactory((activity?.application as MMApplication).storeRepository)
    }
    private val categoriesViewModel: CategoriesViewModel by activityViewModels {
        CategoriesViewModelFactory(
            (activity?.application as MMApplication).categoryRepository,
            (activity?.application as MMApplication).itemRepository
        )
    }
    private val paymentsViewModel: PaymentsViewModel by activityViewModels {
        PaymentsViewModelFactory((activity?.application as MMApplication).paymentRepository)
    }

    // 支出ID
    private var id: Long? = null

    private lateinit var itemListAdapter: ExpenseItemListAdapter
    private lateinit var paymentListAdapter: ExpensePaymentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        _binding = FragmentExpenseDetailBinding.inflate(inflater, container, false)

        itemListAdapter = ExpenseItemListAdapter(categoriesViewModel)
        paymentListAdapter = ExpensePaymentListAdapter(paymentsViewModel)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 前画面から渡された値を取得
        val bundle = arguments

        if (bundle != null) {
            // 支出ID取得
            id = bundle.get("id") as Long?
        }

        lifecycleScope.launch {

            if (id == null) {
                // 新規作成時

                // 初期化
                expenseDetail.initExpenseDetail()

                // 削除ボタンを非表示にする
                binding.deleteExpense.visibility = View.GONE

            } else {
                // 既存の支出詳細表示時

                // 支出詳細取得
                expenseDetail.getDetailById(id!!)

                // 店舗表示
                // storeNameがnullならstoreIdをもとに取得する
                binding.storeName.text = expenseDetail.getStoreName() ?: expenseDetail.getStoreId()
                    ?.let { storesViewModel.findById(it).name }

                // 品物リスト取得
                expenseDetail.getItemList(id!!)

                // 支払いリスト取得
                expenseDetail.getPaymentList(id!!)

                // メモ表示
                binding.note.setText(expenseDetail.getNote())
            }

            // 日付表示
            binding.purchasedAt.text = DateTimeFormatter.ofPattern(DateUtil.DATE_EDMMMYYYY)
                .format(expenseDetail.getPurchasedAt())

            // 日付欄をタップしたらカレンダーダイアログを表示する
            binding.purchasedAt.setOnClickListener {
                DatePickerDialogFragment(expenseDetail.getPurchasedAt().toString()).show(
                    childFragmentManager,
                    null
                )
            }

            // 店舗欄をタップしたら店舗リストダイアログを表示する
            binding.storeName.setOnClickListener {
                EnterStoreDialogFragment(
                    expenseDetail.expenseDetail.value?.storeId,
                    expenseDetail.expenseDetail.value?.storeName
                ).show(childFragmentManager, null)
            }
        }

        // カテゴリリストの監視開始
        categoriesViewModel.categoryList.observe(viewLifecycleOwner, {

            // 品物追加アイコンをタップしたら品物追加ダイアログを表示する
            binding.addItemBtn.setOnClickListener {
                AddItemDialogFragment(null, null, categoriesViewModel.getCategoriesAsFilter()).show(
                    childFragmentManager,
                    null
                )
            }
        })

        // 決済方法リストの監視開始
        paymentsViewModel.paymentList.observe(viewLifecycleOwner, {

            // 支払い追加アイコンをタップしたら支払い追加ダイアログを表示する
            binding.addPaymentBtn.setOnClickListener {
                AddPaymentDialogFragment(
                    null,
                    null,
                    paymentsViewModel.getPaymentsAsFilter()
                ).show(
                    childFragmentManager,
                    null
                )
            }
        })

        // 品物リストの監視開始
        expenseDetail.itemList.observe(viewLifecycleOwner, { itemList ->

            // 品物リストをセットする
            itemListAdapter.submitList(itemList)

            // 品物の合計額表示
            updateItemTotal()
        })

        // 品物リストの表示設定
        binding.itemList.adapter = itemListAdapter
        val itemLinearLayoutManager = LinearLayoutManager(view.context)
        binding.itemList.layoutManager = itemLinearLayoutManager
        binding.itemList.addItemDecoration(
            DividerItemDecoration(
                view.context,
                itemLinearLayoutManager.orientation
            )
        )

        // 品物リストのクリックイベントを設定
        itemListAdapter.setOnExpenseItemClickListener(
            object : ExpenseItemListAdapter.OnExpenseItemClickListener {
                override fun onItemClick(position: Int, item: ItemEntity) {
                    AddItemDialogFragment(
                        position,
                        item,
                        categoriesViewModel.getCategoriesAsFilter()
                    ).show(
                        childFragmentManager,
                        null
                    )
                }
            }
        )

        // 支払いリストの監視開始
        expenseDetail.paymentList.observe(viewLifecycleOwner, { paymentList ->

            // 支払いリストをセットする
            paymentListAdapter.submitList(paymentList)

            // 支払いの合計額表示
            updatePaymentTotal()
        })

        // 支払いリストの表示設定
        binding.paymentList.adapter = paymentListAdapter
        val paymentLinearLayoutManager = LinearLayoutManager(view.context)
        binding.paymentList.layoutManager = paymentLinearLayoutManager
        binding.paymentList.addItemDecoration(
            DividerItemDecoration(
                view.context,
                paymentLinearLayoutManager.orientation
            )
        )

        // 支払いリストのクリックイベントを設定
        paymentListAdapter.setOnExpensePaymentClickListener(
            object : ExpensePaymentListAdapter.OnExpensePaymentClickListener {
                override fun onItemClick(position: Int, ep: ExpensePaymentEntity) {
                    AddPaymentDialogFragment(
                        position,
                        ep,
                        paymentsViewModel.getPaymentsAsFilter()
                    ).show(childFragmentManager, null)
                }
            }
        )

        // 削除ボタンをタップしたら確認ダイアログを表示する
        binding.deleteExpense.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("Delete this expense?")
                .setPositiveButton("OK") { _, _ ->
                    lifecycleScope.launch {
                        // 支出詳細をDB上から削除するAPIを投げる
                        expenseDetail.delete()
                        // 支出一覧画面に遷移する
                        startActivity(Intent(context, MainActivity::class.java))
                    }
                }
                .setNegativeButton("Cancel") { _, _ ->
                    // なにもしない
                }
                .show()
        }

    }

    /**
     * メニュー表示
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
    }

    /**
     * メニュータップ時の処理設定
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {

                // メモをセット
                expenseDetail.setNote(binding.note.text.toString())

                // バリデーションチェック
                if (!validationCheck()) {
                    // 失敗したら何もせず終了
                    return true
                }

                lifecycleScope.launch {
                    // 支出IDがnullなら新規作成API、それ以外なら更新APIを投げる
                    if (id == null) {
                        // 支出詳細をDB上で新規作成するAPIを投げる
                        expenseDetail.create()
                    } else {
                        // 支出詳細をDB上で更新するAPIを投げる
                        expenseDetail.update()
                    }
                }
                // 支出一覧画面に遷移する
                startActivity(Intent(context, MainActivity::class.java))
            }
            android.R.id.home -> {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        return true
    }

    /**
     * 日付ダイアログで選択したときに呼ばれる from DatePickerDialogFragment
     */
    override fun selectDate(year: Int, month: Int, day: Int) {
        val dateTime = DateUtil.parseLocalDateTimeFromInt(year, month, day)
        binding.purchasedAt.text = DateUtil.formatDate(dateTime, DateUtil.DATE_EDMMMYYYY)
        expenseDetail.setPurchasedAt(
            LocalDateTime.of(
                LocalDate.of(year, month, day),
                LocalTime.now()
            )
        )
    }

    /**
     * 店舗を入力したときに呼ばれる from EnterStoreDialogFragment
     */
    override fun selectStore(id: Long?, name: String) {
        // 店舗表示
        binding.storeName.text = name

        if (id == null) {
            // idがnullなら手入力なので店舗名を記憶する
            expenseDetail.setStoreName(name)
        } else {
            // それ以外ならidから辿れるのでidだけ記憶する
            expenseDetail.setStoreId(id)
        }
    }

    /**
     * 品物を入力したときに呼ばれる from AddItemDialogFragment
     */
    override fun addItem(item: ItemEntity) {
        expenseDetail.addItem(item)
        binding.itemList.adapter?.notifyDataSetChanged()
        // 品物の合計額更新
        updateItemTotal()
    }

    /**
     * 品物を更新したときに呼ばれる from AddItemDialogFragment
     */
    override fun updateItem(position: Int, item: ItemEntity) {
        expenseDetail.updateItem(position, item)
        binding.itemList.adapter?.notifyDataSetChanged()
        // 品物の合計額更新
        updateItemTotal()
    }

    /**
     * 品物を削除したときに呼ばれる from AddItemDialogFragment
     */
    override fun deleteItem() {
        binding.itemList.adapter?.notifyDataSetChanged()
        // 品物の合計額更新
        updateItemTotal()
    }

    /**
     * 支払いを入力したときに呼ばれる from AddPaymentDialogFragment
     */
    override fun addPayment(ep: ExpensePaymentEntity) {
        expenseDetail.addPayment(ep)
        binding.paymentList.adapter?.notifyDataSetChanged()
        // 支払いの合計額更新
        updatePaymentTotal()
    }

    /**
     * 支払いを更新したときに呼ばれる from AddPaymentDialogFragment
     */
    override fun updatePayment(position: Int, ep: ExpensePaymentEntity) {
        expenseDetail.updatePayment(position, ep)
        binding.paymentList.adapter?.notifyDataSetChanged()
        // 支払いの合計額更新
        updatePaymentTotal()
    }

    /**
     * 支払いを削除したときに呼ばれる from AddPaymentDialogFragment
     */
    override fun deletePayment() {
        binding.paymentList.adapter?.notifyDataSetChanged()
        // 支払いの合計額更新
        updatePaymentTotal()
    }

    /**
     * 品物の合計額を計算して表示
     */
    private fun updateItemTotal() {

        val total = expenseDetail.itemList.value?.fold(BigDecimal.ZERO) { acc, item ->
            acc + item.price
        }
        binding.numTotalItem.text = total?.let { Utils.convertToStrDecimal(it) }
    }

    /**
     * 支払いの合計額を計算して表示
     */
    private fun updatePaymentTotal() {

        val total = expenseDetail.paymentList.value?.fold(BigDecimal.ZERO) { acc, payment ->
            acc + payment.total
        }
        binding.numTotalPayment.text = total?.let { Utils.convertToStrDecimal(it) }
    }

    /**
     * 入力内容のバリデーションチェック
     */
    private fun validationCheck(): Boolean {

        var isValidated = true
        var message = ""

        // 店舗名が入力されていない
        if (TextUtils.isEmpty(binding.storeName.text)) {
            isValidated = false
            message += getString(R.string.store_is_empty)
        }
        // 品物が0件
        if (expenseDetail.itemList.value.isNullOrEmpty()) {
            isValidated = false
            message += getString(R.string.item_is_empty)
        }
        // 支払いが0件
        if (expenseDetail.paymentList.value.isNullOrEmpty()) {
            isValidated = false
            message += getString(R.string.payment_is_empty)
        }
        // 品物の合計と支払いの合計が一致しているか
        if (!binding.numTotalItem.text.equals(binding.numTotalPayment.text)) {
            isValidated = false
            message += getString(R.string.total_are_not_corresponding)
        }

        // バリデーションチェックがfalseならエラートーストを出す
        if (!isValidated) {
            Toast.makeText(activity, message, Toast.LENGTH_LONG)
                .show()
        }
        return isValidated
    }

}