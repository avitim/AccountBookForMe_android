package com.example.accountbookforme.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.model.Expense
import com.example.accountbookforme.repository.ExpenseRepository
import com.example.accountbookforme.util.RestUtil
import kotlinx.coroutines.launch

class ExpensesViewModel : ViewModel() {

    private val expenseRepository: ExpenseRepository = RestUtil.retrofit.create(ExpenseRepository::class.java)

    // 支出リスト
    var expenseList: MutableLiveData<List<Expense>> = MutableLiveData()

    init {
        loadExpenseList()
    }

    private fun loadExpenseList() {

        viewModelScope.launch {
            try {
                val request = expenseRepository.getAll()
                if (request.isSuccessful) {
                    expenseList.value = request.body()
                } else {
                    Log.e("ExpenseViewModel", "Something is wrong: $request")
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }
}