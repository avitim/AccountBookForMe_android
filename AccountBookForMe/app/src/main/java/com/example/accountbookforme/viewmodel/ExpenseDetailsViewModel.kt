package com.example.accountbookforme.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.accountbookforme.database.entity.ExpenseDetailEntity
import com.example.accountbookforme.database.repository.ExpenseRepository
import com.example.accountbookforme.model.Item
import com.example.accountbookforme.model.Payment
import java.math.BigDecimal
import java.time.LocalDateTime

class ExpenseDetailsViewModel(private val repository: ExpenseRepository) : ViewModel() {

    // 支出詳細
    var expenseDetail: MutableLiveData<ExpenseDetailEntity> = MutableLiveData()

    /**
     * 空のExpenseDetail生成
     */
    fun createExpenseDetail() {
        expenseDetail.value = ExpenseDetailEntity()
    }

    /**
     * 支出詳細の取得
     */
    suspend fun getDetailById(id: Long) {
        expenseDetail.value = repository.getDetailById(id)
    }

    /**
     * 支出詳細の新規作成
     */
    suspend fun create() = expenseDetail.value?.let { repository.create(it) }

    /**
     * 支出詳細の更新
     */
    suspend fun update() = expenseDetail.value?.let { repository.update(it) }

    /**
     * 支出詳細の削除
     */
    suspend fun delete() = expenseDetail.value?.id?.let { repository.deleteById(it) }

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
     * 品物リストを返す
     */
    fun getItemList(): List<Item>? {
//        return expenseDetail.value?.itemList?.toList()
        // TODO: 要実装
        return arrayListOf()
    }

    /**
     * 支払いリストを返す
     */
    fun getPaymentList(): List<Payment>? {
//        return expenseDetail.value?.paymentList?.toList()
        // TODO: 要実装
        return arrayListOf()
    }

    /**
     * アイテムを位置で指定して名前をセットする
     */
    fun setItemName(position: Int, itemName: String) {
        try {
            // TODO: 要実装
//            val item = expenseDetail.value?.itemList?.get(position)
//            if (item != null) {
//                item.name = itemName
//            }
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * アイテムをIDで指定して値段をセットする
     */
    fun setItemPrice(position: Int, itemPrice: BigDecimal) {
        try {
            // TODO: 要実装
//            val item = expenseDetail.value?.itemList?.get(position)
//            if (item != null) {
//                item.price = itemPrice
//            }
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * アイテムをIDで指定してカテゴリIDをセットする
     */
    fun setItemCategory(position: Int, categoryId: Long) {
        try {
            // TODO: 要実装
//            val item = expenseDetail.value?.itemList?.get(position)
//            if (item != null) {
//                item.categoryId = categoryId
//            }
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * 品物追加
     */
    fun addItem(item: Item) {
        // TODO: 要実装
//        expenseDetail.value?.itemList?.add(item)
    }

    /**
     * 品物削除
     */
    fun removeItem(position: Int) {
        try {
            // TODO: 要実装
//            expenseDetail.value?.itemList?.removeAt(position)
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * 品物削除して、削除済みリストに追加
     */
    fun deleteItem(position: Int) {
        try {
            // TODO: 要実装
//            val item = expenseDetail.value?.itemList?.get(position)
//            expenseDetail.value?.itemList?.removeAt(position)
//            if (item != null) {
//                expenseDetail.value?.deletedItemList?.add(item.id!!)
//            }
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * 支払いを位置で指定して決済方法IDをセットする
     */
    fun setPaymentMethod(position: Int, paymentMethodId: Long) {
        try {
            // TODO: 要実装
//            val payment = expenseDetail.value?.paymentList?.get(position)
//            if (payment != null) {
//                payment.paymentId = paymentMethodId
//            }
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * 支払いを位置で指定して金額をセットする
     */
    fun setPaymentTotal(position: Int, paymentTotal: BigDecimal) {
        try {
            // TODO: 要実装
//            val payment = expenseDetail.value?.paymentList?.get(position)
//            if (payment != null) {
//                payment.total = paymentTotal
//            }
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * 支払い追加
     */
    fun addPayment(payment: Payment) {
        // TODO: 要実装
//        expenseDetail.value?.paymentList?.add(payment)
    }

    /**
     * 支払い削除
     */
    fun removePayment(position: Int) {
        try {
            // TODO: 要実装
//            expenseDetail.value?.paymentList?.removeAt(position)
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * 支払い削除して、削除済みリストに追加
     */
    fun deletePayment(position: Int) {
        try {
            // TODO: 要実装
//            val payment = expenseDetail.value?.paymentList?.get(position)
//            expenseDetail.value?.paymentList?.removeAt(position)
//            if (payment != null) {
//                expenseDetail.value?.deletedPaymentList?.add(payment.id!!)
//            }
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }
}

class ExpenseDetailsViewModelFactory(private val repository: ExpenseRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseDetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}