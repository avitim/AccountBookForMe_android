package com.example.accountbookforme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.database.repository.ExpensePaymentRepository
import com.example.accountbookforme.database.repository.ExpenseRepository
import com.example.accountbookforme.model.Expense
import com.example.accountbookforme.model.Total
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ExpensesViewModel(
    private val expenseRepository: ExpenseRepository,
    private val epRepository: ExpensePaymentRepository
) : ViewModel() {

    // 支出リスト
    var expenseList: LiveData<List<Expense>> = expenseRepository.expenseList.asLiveData()

    // 店舗ごとの支出額リスト
    var totalStoreList: MutableLiveData<List<Total>> = MutableLiveData()

    // 店舗ごとの支出リスト
    var storeExpenseList: MutableLiveData<List<Expense>> = MutableLiveData()

    /**
     * 支出の総額取得
     */
    suspend fun calcTotal(expenseId: Long): BigDecimal = epRepository.calcTotalByExpenseId(expenseId)

    /**
     * 店舗IDから支出リスト取得
     */
    fun findByStoreId(storeId: Long) = viewModelScope.launch {
        expenseRepository.findByStoreId(storeId)
    }
}

class ExpensesViewModelFactory(
    private val expenseRepository: ExpenseRepository,
    private val epRepository: ExpensePaymentRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpensesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpensesViewModel(expenseRepository, epRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}