package com.example.accountbookforme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.accountbookforme.model.ExpenseListItem

class ExpensesViewModel(application: Application) : AndroidViewModel(application) {

    // 監視対象のLiveData
    var expensesLiveData: MutableLiveData<List<ExpenseListItem>> = MutableLiveData()

    // ViewModel初期化時に呼ばれる
    init {
        loadExpenseList()
    }

    private fun loadExpenseList() {
        // viewModelScope->ViewModel.onCleared() のタイミングでキャンセルされるCoroutineScope
        // TODO: のちのちLiveDataを使う方法で実装する
//        viewModelScope.launch {
//            try {
//                val request = repository.getAll()
//                if (request.isSuccessful) {
//                    // データを取得したらLiveDataを更新
//                    expensesLiveData.postValue(request.body())
//                }
//            } catch (e: Exception) {
//                e.stackTrace
//            }
//        }
    }
}