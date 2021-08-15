package com.example.accountbookforme.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.model.ExpenseDetail
import com.example.accountbookforme.model.Item
import com.example.accountbookforme.repository.ExpenseRepository
import com.example.accountbookforme.util.RestUtil
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ExpenseDetailViewModel : ViewModel() {

    private val expenseRepository: ExpenseRepository = RestUtil.retrofit.create(ExpenseRepository::class.java)

    // 支出詳細
    var expenseDetail: MutableLiveData<ExpenseDetail> = MutableLiveData()

    // 支出詳細の取得
    fun getExpenseDetail(id: Long) {

        viewModelScope.launch {
            try {
                val request = expenseRepository.getDetailById(id)
                if (request.isSuccessful) {
                    Log.i("ExpenseDetailViewModel", "There is no problem: $request")
                    expenseDetail.value = request.body()
                } else {
                    Log.e("ExpenseDetailViewModel", "Something is wrong: $request")
                }
            } catch (e: Exception) {
                Log.e("ExpenseDetailViewModel", "Something is wrong: $e")
            }
        }
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

    // 品物追加
    fun addItem(item: Item) {
        expenseDetail.value?.itemList?.add(item)
    }

}