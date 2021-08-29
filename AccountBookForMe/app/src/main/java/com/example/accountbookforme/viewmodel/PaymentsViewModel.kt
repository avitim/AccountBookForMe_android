package com.example.accountbookforme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.database.entity.PaymentEntity
import com.example.accountbookforme.database.repository.PaymentRepository
import com.example.accountbookforme.model.Filter
import kotlinx.coroutines.launch

class PaymentsViewModel(private val repository: PaymentRepository) : ViewModel() {

    // 決済方法一覧
    var paymentList: LiveData<List<PaymentEntity>> = repository.paymentList.asLiveData()

    /**
     * IDから決済方法を取得
     */
    fun getById(id: Long) = paymentList.value?.find { payment -> payment.id == id }

    /**
     * 決済方法一覧をFilter型のリストで取得
     */
    fun getPaymentsAsFilter(): List<Filter> {
        val filterList: MutableList<Filter> = arrayListOf()
        paymentList.value?.forEach { payment ->
            filterList.add(Filter(payment.id, payment.name))
        }
        return filterList
    }

    /**
     * カテゴリ新規作成
     */
    fun create(name: String) = viewModelScope.launch {
        repository.create(PaymentEntity(name = name))
    }

    /**
     * カテゴリ更新
     */
    fun update(filter: Filter) = viewModelScope.launch {
        filter.id?.let { PaymentEntity(id = it, name = filter.name) }?.let { repository.update(it) }
    }

    /**
     * カテゴリ削除
     */
    fun deleteById(id: Long) = viewModelScope.launch {
        repository.deleteById(id)
    }
}

class PaymentsViewModelFactory(private val repository: PaymentRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PaymentsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}