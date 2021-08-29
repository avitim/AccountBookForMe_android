package com.example.accountbookforme.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.model.ExpenseDetail
import com.example.accountbookforme.model.Item
import com.example.accountbookforme.model.Payment
import com.example.accountbookforme.database.repository.ExpenseRepository
import com.example.accountbookforme.util.RestUtil
import kotlinx.coroutines.launch
import java.lang.IndexOutOfBoundsException
import java.math.BigDecimal

class ExpenseDetailViewModel : ViewModel() {

    private val expenseRepository: ExpenseRepository =
        RestUtil.retrofit.create(ExpenseRepository::class.java)

    // 支出詳細
    var expenseDetail: MutableLiveData<ExpenseDetail> = MutableLiveData()

    /**
     * 空のExpenseDetail生成
     */
    fun createExpenseDetail() {
        expenseDetail.value = ExpenseDetail()
    }

    /**
     * 支出詳細の取得
     */
    fun getExpenseDetail(id: Long) {

        viewModelScope.launch {
            try {
                val request = expenseRepository.getDetailById(id)
                if (request.isSuccessful) {
                    expenseDetail.value = request.body()
                } else {
                    Log.e("ExpenseDetailViewModel", "Not successful: $request")
                }
            } catch (e: Exception) {
                Log.e("ExpenseDetailViewModel", "Something is wrong: $e")
            }
        }
    }

    /**
     * 支出詳細の新規作成
     */
    fun create(): LiveData<Boolean> {

        val isSuccessful = MutableLiveData<Boolean>()

        viewModelScope.launch {
            try {
                val response = expenseDetail.value?.let { expenseRepository.create(it) }
                if (response != null) {
                    if (response.isSuccessful) {
                        isSuccessful.postValue(true)
                    } else {
                        isSuccessful.postValue(false)
                        Log.e("ExpenseDetailViewModel", "Not successful: $response")
                    }
                }
            } catch (e: Exception) {
                isSuccessful.postValue(false)
                Log.e("ExpenseDetailViewModel", "Something is wrong: $e")
            }
        }

        return isSuccessful
    }

    /**
     * 支出詳細の更新
     */
    fun update(): LiveData<Boolean> {

        val isSuccessful = MutableLiveData<Boolean>()

        viewModelScope.launch {
            try {
                val response = expenseDetail.value?.let { expenseRepository.update(it) }
                if (response != null) {
                    if (response.isSuccessful) {
                        isSuccessful.postValue(true)
                    } else {
                        isSuccessful.postValue(false)
                        Log.e("ExpenseDetailViewModel", "Not successful: $response")
                    }
                }
            } catch (e: Exception) {
                isSuccessful.postValue(false)
                Log.e("ExpenseDetailViewModel", "Something is wrong: $e")
            }
        }

        return isSuccessful
    }

    /**
     * 支出詳細の削除
     */
    fun delete(): LiveData<Boolean> {

        val isSuccessful = MutableLiveData<Boolean>()

        viewModelScope.launch {
            try {
                val response = expenseDetail.value?.id?.let { expenseRepository.delete(it) }
                if (response != null) {
                    if (response.isSuccessful) {
                        isSuccessful.postValue(true)
                    } else {
                        isSuccessful.postValue(false)
                        Log.e("ExpenseDetailViewModel", "Not successful: $response")
                    }
                }
            } catch (e: Exception) {
                isSuccessful.postValue(false)
                Log.e("ExpenseDetailViewModel", "Something is wrong: $e")
            }
        }

        return isSuccessful
    }

    /**
     * 購入日を返す
     */
    fun getPurchasedAt(): String? {
        return expenseDetail.value?.purchasedAt
    }

    /**
     * 品物リストを返す
     */
    fun getItemList(): List<Item>? {
        return expenseDetail.value?.itemList?.toList()
    }

    /**
     * 支払いリストを返す
     */
    fun getPaymentList(): List<Payment>? {
        return expenseDetail.value?.paymentList?.toList()
    }

    /**
     * アイテムを位置で指定して名前をセットする
     */
    fun setItemName(position: Int, itemName: String) {
        try {
            val item = expenseDetail.value?.itemList?.get(position)
            if (item != null) {
                item.name = itemName
            }
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * アイテムをIDで指定して値段をセットする
     */
    fun setItemPrice(position: Int, itemPrice: BigDecimal) {
        try {
            val item = expenseDetail.value?.itemList?.get(position)
            if (item != null) {
                item.price = itemPrice
            }
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * アイテムをIDで指定してカテゴリIDをセットする
     */
    fun setItemCategory(position: Int, categoryId: Long) {
        try {
            val item = expenseDetail.value?.itemList?.get(position)
            if (item != null) {
                item.categoryId = categoryId
            }
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * 品物追加
     */
    fun addItem(item: Item) {
        expenseDetail.value?.itemList?.add(item)
    }

    /**
     * 品物削除
     */
    fun removeItem(position: Int) {
        try {
            expenseDetail.value?.itemList?.removeAt(position)
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * 品物削除して、削除済みリストに追加
     */
    fun deleteItem(position: Int) {
        try {
            val item = expenseDetail.value?.itemList?.get(position)
            expenseDetail.value?.itemList?.removeAt(position)
            if (item != null) {
                expenseDetail.value?.deletedItemList?.add(item.id!!)
            }
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * 支払いを位置で指定して決済方法IDをセットする
     */
    fun setPaymentMethod(position: Int, paymentMethodId: Long) {
        try {
            val payment = expenseDetail.value?.paymentList?.get(position)
            if (payment != null) {
                payment.paymentId = paymentMethodId
            }
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * 支払いを位置で指定して金額をセットする
     */
    fun setPaymentTotal(position: Int, paymentTotal: BigDecimal) {
        try {
            val payment = expenseDetail.value?.paymentList?.get(position)
            if (payment != null) {
                payment.total = paymentTotal
            }
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * 支払い追加
     */
    fun addPayment(payment: Payment) {
        expenseDetail.value?.paymentList?.add(payment)
    }

    /**
     * 支払い削除
     */
    fun removePayment(position: Int) {
        try {
            expenseDetail.value?.paymentList?.removeAt(position)
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

    /**
     * 支払い削除して、削除済みリストに追加
     */
    fun deletePayment(position: Int) {
        try {
            val payment = expenseDetail.value?.paymentList?.get(position)
            expenseDetail.value?.paymentList?.removeAt(position)
            if (payment != null) {
                expenseDetail.value?.deletedPaymentList?.add(payment.id!!)
            }
        } catch (e: IndexOutOfBoundsException) {
            // 何もしない
        }
    }

}