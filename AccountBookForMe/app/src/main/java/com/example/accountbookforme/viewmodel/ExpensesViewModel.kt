package com.example.accountbookforme.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.model.Expense
import com.example.accountbookforme.model.Total
import com.example.accountbookforme.repository.ExpenseRepository
import com.example.accountbookforme.util.RestUtil
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ExpensesViewModel : ViewModel() {

    private val expenseRepository: ExpenseRepository =
        RestUtil.retrofit.create(ExpenseRepository::class.java)

    // 支出リスト
    var expenseList: MutableLiveData<List<Expense>> = MutableLiveData()

    // 決済方法ごとの支出額リスト
    var totalPaymentList: MutableLiveData<List<Total>> = MutableLiveData()

    // 店舗ごとの支出額リスト
    var totalStoreList: MutableLiveData<List<Total>> = MutableLiveData()

    // 店舗ごとの支出リスト
    var expenseStoreList: MutableLiveData<List<Expense>> = MutableLiveData()

    init {
        loadExpenseList()
        getTotalPaymentList()
        getTotalStoreList()
    }

    /**
     * 支出リスト取得
     */
    private fun loadExpenseList() {

        viewModelScope.launch {
            try {
                val request = expenseRepository.getAll()
                if (request.isSuccessful) {
                    expenseList.value = request.body()
                } else {
                    Log.e("ExpenseViewModel", "Not successful: $request")
                }
            } catch (e: Exception) {
                Log.e("ExpenseViewModel", "Something is wrong: $e")
            }
        }
    }

    /**
     * 決済方法ごとの支出額リスト取得
     */
    private fun getTotalPaymentList() {

        viewModelScope.launch {
            try {
                val request = expenseRepository.getTotalPaymentList()
                if (request.isSuccessful) {
                    totalPaymentList.value = request.body()
                } else {
                    Log.e("ExpenseViewModel", "Not successful: $request")
                }
            } catch (e: Exception) {
                Log.e("ExpenseViewModel", "Something is wrong: $e")
            }
        }
    }

    /**
     * 店舗ごとの支出額リスト取得
     */
    private fun getTotalStoreList() {

        viewModelScope.launch {
            try {
                val request = expenseRepository.getTotalStoreList()
                if (request.isSuccessful) {
                    totalStoreList.value = request.body()
                } else {
                    Log.e("ExpenseViewModel", "Not successful: $request")
                }
            } catch (e: Exception) {
                Log.e("ExpenseViewModel", "Something is wrong: $e")
            }
        }
    }

    /**
     * 店舗IDから品物リスト取得
     */
    fun getExpenseListByStoreId(storeId: Long) {

        viewModelScope.launch {
            try {
                val request = expenseRepository.getByStoreId(storeId)
                if (request.isSuccessful) {
                    expenseStoreList.value = request.body()
                } else {
                    Log.e("CategoriesViewModel", "Not successful: $request")
                }
            } catch (e: Exception) {
                Log.e("CategoriesViewModel", "Something is wrong: $e")
            }
        }
    }

    /**
     * すべての総額を計算
     */
    fun calcAllTotal(): BigDecimal? = expenseList.value?.fold(BigDecimal.ZERO) { acc, expense ->
        acc + expense.total
    }

    /**
     * 支払い額をもとに支出の総額を計算
     */
    fun calcTotalPayment(): BigDecimal? =
        totalPaymentList.value?.fold(BigDecimal.ZERO) { acc, expense ->
            acc + expense.total
        }

    /**
     * 店舗ごとの金額をもとに支出の総額を計算
     */
    fun calcTotalStore(): BigDecimal? =
        totalStoreList.value?.fold(BigDecimal.ZERO) { acc, expense ->
            acc + expense.total
        }

    /**
     * 店舗ごとの金額をもとに支出の総額を計算
     */
    fun calcTotalByStore(): BigDecimal? =
        expenseStoreList.value?.fold(BigDecimal.ZERO) { acc, expense ->
            acc + expense.total
        }
}