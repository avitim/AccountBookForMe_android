package com.example.accountbookforme.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Transaction
import com.example.accountbookforme.database.entity.ExpenseDetailEntity
import com.example.accountbookforme.database.entity.ExpensePaymentEntity
import com.example.accountbookforme.database.entity.ItemEntity
import com.example.accountbookforme.database.repository.ExpensePaymentRepository
import com.example.accountbookforme.database.repository.ExpenseRepository
import com.example.accountbookforme.database.repository.ItemRepository
import java.time.LocalDateTime

class ExpenseDetailsViewModel(
    private val expenseRepository: ExpenseRepository,
    private val itemRepository: ItemRepository,
    private val epRepository: ExpensePaymentRepository
) : ViewModel() {

    // 支出詳細
    val expenseDetail = MutableLiveData<ExpenseDetailEntity>()

    // 品物リスト
    val itemList = MutableLiveData<MutableList<ItemEntity>>()

    // 支払いリスト
    val paymentList = MutableLiveData<MutableList<ExpensePaymentEntity>>()

    // 品物削除リスト
    // 要素: ItemEntity.id: Long
    private var deleteItemList: MutableList<Long> = mutableListOf()

    // 支払い削除リスト
    // 要素: ExpensePaymentEntity.id: Long
    private var deletePaymentList: MutableList<Long> = mutableListOf()

    /**
     * 初期化
     */
    fun initExpenseDetail() {
        expenseDetail.value = ExpenseDetailEntity()
        itemList.value = mutableListOf()
        paymentList.value = mutableListOf()
    }

    /**
     * 支出詳細の取得
     */
    suspend fun getDetailById(id: Long) {
        expenseDetail.value = expenseRepository.getDetailById(id)
    }

    /**
     * 支出詳細の新規作成
     */
    @Transaction
    suspend fun create() {

        expenseDetail.value?.let {

            // 支出詳細の作成
            val expenseId = expenseRepository.create(it)

            itemList.value?.forEach { item ->
                // 支出IDをセット
                item.expenseId = expenseId
                // 品物の作成
                itemRepository.insert(item)
            }

            paymentList.value?.forEach { payment ->
                // 支出IDをセット
                payment.expenseId = expenseId
                // 支払いの作成
                epRepository.insert(payment)
            }
        }
    }

    /**
     * 支出詳細の更新
     */
    @Transaction
    suspend fun update() {

        expenseDetail.value?.let { expenseDetail ->

            // 支出詳細の更新
            expenseRepository.update(expenseDetail)

            // 品物の更新 or 作成
            itemList.value?.forEach { item ->

                if (item.id == 0L) {
                    // 支出IDをセット
                    item.expenseId = expenseDetail.id
                    // 新規作成
                    itemRepository.insert(item)
                } else {
                    // 更新
                    itemRepository.update(item)
                }
            }

            // 支払いの更新 or 作成
            paymentList.value?.forEach { payment ->

                if (payment.id == 0L) {
                    // 支出IDをセット
                    payment.expenseId = expenseDetail.id
                    // 新規作成
                    epRepository.insert(payment)
                } else {
                    // 更新
                    epRepository.update(payment)
                }
            }
        }

        // 品物の削除
        deleteItemList.forEach { itemId ->
            itemRepository.deleteById(itemId)
        }

        // 支払いの削除
        deletePaymentList.forEach { epId ->
            epRepository.deleteById(epId)
        }
    }

    /**
     * 支出詳細の削除
     * DBの設定でexpense.idをexpense_idに持つitemとexpensePaymentは自動で削除される
     */
    @Transaction
    suspend fun delete() {

        expenseDetail.value?.id?.let {

            // 支出詳細の削除
            expenseRepository.deleteById(it)

//            // 品物の削除
//            itemRepository.findByExpenseId(it).forEach { item ->
//                itemRepository.deleteById(item.id)
//            }
//
//            // 支払いの削除
//            epRepository.findByExpenseId(it).forEach { payment ->
//                epRepository.deleteById(payment.id)
//            }
        }
    }

    /**
     * 購入日を返す
     */
    fun getPurchasedAt() = expenseDetail.value?.purchasedAt

    /**
     * 購入日を設定する
     */
    fun setPurchasedAt(date: LocalDateTime) {
        expenseDetail.value?.purchasedAt = date
    }

    /**
     * メモを返す
     */
    fun getNote() = expenseDetail.value?.note

    /**
     * メモを設定する
     */
    fun setNote(note: String) {
        expenseDetail.value?.note = note
    }

    /**
     * 品物リストを取得
     */
    suspend fun getItemList(expenseId: Long) {
        itemList.postValue(itemRepository.findByExpenseId(expenseId) as MutableList<ItemEntity>?)
    }

    /**
     * 支払いリストを取得
     */
    suspend fun getPaymentList(expenseId: Long) {
        paymentList.postValue(epRepository.findByExpenseId(expenseId) as MutableList<ExpensePaymentEntity>)
    }

    /**
     * 品物追加
     */
    fun addItem(item: ItemEntity) {
        itemList.value?.add(item)
    }

    /**
     * 品物更新
     */
    fun updateItem(position: Int, item: ItemEntity) {
        try {
            val oldItem = itemList.value?.get(position)
            if (oldItem != null) {
                if (oldItem.name != item.name) {
                    oldItem.name = item.name
                }
                if (oldItem.price != item.price) {
                    oldItem.price = item.price
                }
                if (oldItem.categoryId != item.categoryId) {
                    oldItem.categoryId = item.categoryId
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * 品物削除
     */
    fun removeItem(position: Int) {
        try {
            itemList.value?.removeAt(position)
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * 品物削除して、削除済みリストに追加
     */
    fun deleteItem(position: Int) {
        try {
            val item = itemList.value?.get(position)
            itemList.value?.removeAt(position)
            if (item != null) {
                deleteItemList.add(item.id)
            }
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * 支払い追加
     */
    fun addPayment(ep: ExpensePaymentEntity) = paymentList.value?.add(ep)

    /**
     * 支払い更新
     */
    fun updatePayment(position: Int, ep: ExpensePaymentEntity) {
        try {
            val oldPayment = paymentList.value?.get(position)
            if (oldPayment != null) {
                if (oldPayment.total != ep.total) {
                    oldPayment.total = ep.total
                }
                if (oldPayment.paymentId != ep.paymentId) {
                    oldPayment.paymentId = ep.paymentId
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * 支払い削除
     */
    fun removePayment(position: Int) {
        try {
            paymentList.value?.removeAt(position)
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * 支払い削除して、削除済みリストに追加
     */
    fun deletePayment(position: Int) {
        try {
            val payment = paymentList.value?.get(position)
            paymentList.value?.removeAt(position)
            if (payment != null) {
                deletePaymentList.add(payment.id)
            }
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }
}

class ExpenseDetailsViewModelFactory(
    private val expenseRepository: ExpenseRepository,
    private val itemRepository: ItemRepository,
    private val epRepository: ExpensePaymentRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseDetailsViewModel(expenseRepository, itemRepository, epRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}