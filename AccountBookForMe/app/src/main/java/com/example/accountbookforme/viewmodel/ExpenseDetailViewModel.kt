package com.example.accountbookforme.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.model.ExpenseDetail
import com.example.accountbookforme.model.Item
import com.example.accountbookforme.model.Payment
import com.example.accountbookforme.repository.ExpenseRepository
import com.example.accountbookforme.util.RestUtil
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ExpenseDetailViewModel : ViewModel() {

    private val expenseRepository: ExpenseRepository =
        RestUtil.retrofit.create(ExpenseRepository::class.java)

    // 支出詳細
    var expenseDetail: MutableLiveData<ExpenseDetail> = MutableLiveData()

    // 支出詳細の取得
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

    // 支出詳細の更新
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

    fun getItemList(): List<Item>? {
        return expenseDetail.value?.itemList?.toList()
    }

    fun getPaymentList(): List<Payment>? {
        return expenseDetail.value?.paymentList?.toList()
    }

    // アイテムIDでItem取得
    fun getItemById(id: Long): Item? {

        return expenseDetail.value?.itemList?.find { item ->
            item.id == id
        }
    }

    // アイテムをIDで指定して名前をセットする
    fun setItemName(itemId: Long, itemName: String) {
        val item = getItemById(itemId)
        if (item != null) {
            item.name = itemName
        }
    }

    // アイテムをIDで指定して値段をセットする
    fun setItemPrice(itemId: Long, itemPrice: BigDecimal) {
        val item = getItemById(itemId)
        if (item != null) {
            item.price = itemPrice
        }
    }

    // アイテムをIDで指定してカテゴリIDをセットする
    fun setItemCategory(itemId: Long, categoryId: Long) {
        val item = getItemById(itemId)
        if (item != null) {
            item.categoryId = categoryId
        }
    }

    // 品物追加
    fun addItem(item: Item) {
        expenseDetail.value?.itemList?.add(item)
    }

    // 支出-決済方法IDでItem取得
    fun getPaymentById(expensePaymentId: Long): Payment? {

        return expenseDetail.value?.paymentList?.find { payment ->
            payment.id == expensePaymentId
        }
    }

    // 支払いをIDで指定して決済方法IDをセットする
    fun setPaymentMethod(expensePaymentId: Long, paymentMethodId: Long) {
        val payment = getPaymentById(expensePaymentId)
        if (payment != null) {
            payment.paymentId = paymentMethodId
        }
    }

    // 支払いをIDで指定して金額をセットする
    fun setPaymentTotal(expensePaymentId: Long, paymentTotal: BigDecimal) {
        val payment = getPaymentById(expensePaymentId)
        if (payment != null) {
            payment.total = paymentTotal
        }
    }

    // 支払い追加
    fun addPayment(payment: Payment) {
        expenseDetail.value?.paymentList?.add(payment)
    }

}