package com.example.accountbookforme.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.model.ExpenseDetail
import com.example.accountbookforme.repository.ExpenseRepository
import com.example.accountbookforme.util.RestUtil
import kotlinx.coroutines.launch

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

}