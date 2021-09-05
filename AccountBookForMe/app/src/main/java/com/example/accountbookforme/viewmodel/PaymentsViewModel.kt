package com.example.accountbookforme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.database.entity.PaymentEntity
import com.example.accountbookforme.database.repository.ExpensePaymentRepository
import com.example.accountbookforme.database.repository.PaymentRepository
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.model.Total
import kotlinx.coroutines.launch

class PaymentsViewModel(
    private val paymentRepository: PaymentRepository,
    private val epRepository: ExpensePaymentRepository
) : ViewModel() {

    // 決済方法一覧
    var paymentList: LiveData<List<PaymentEntity>> = paymentRepository.paymentList.asLiveData()

    // 決済方法ごとの総額リスト
    private var totalList = MutableLiveData<List<Total>>()

    /**
     * IDから決済方法を取得
     */
    fun getById(id: Long) = paymentList.value?.find { payment -> payment.id == id }

    /**
     * 決済方法一覧をFilter型のリストで取得
     */
    fun getPaymentsAsFilter(): List<Filter> =
        paymentList.value?.map { payment -> Filter(payment.id, payment.name) }.orEmpty()

    /**
     * カテゴリごとの総額を取得
     */
    suspend fun loadTotalList() {

        totalList.value = paymentList.value?.map { payment ->
            Total(
                id = payment.id,
                name = payment.name,
                total = epRepository.getPaymentTotal(payment.id)
            )
        }
    }

    /**
     * 決済方法ごとの総額リストを返す
     */
    fun getTotalList(): List<Total> = totalList.value.orEmpty()

    /**
     * カテゴリ新規作成
     */
    fun create(name: String) = viewModelScope.launch {
        paymentRepository.create(PaymentEntity(name = name))
    }

    /**
     * カテゴリ更新
     */
    fun update(filter: Filter) = viewModelScope.launch {
        filter.id?.let { PaymentEntity(id = it, name = filter.name) }?.let { paymentRepository.update(it) }
    }

    /**
     * カテゴリ削除
     */
    fun deleteById(id: Long) = viewModelScope.launch {
        paymentRepository.deleteById(id)
    }
}

class PaymentsViewModelFactory(
    private val paymentRepository: PaymentRepository,
    private val epRepository: ExpensePaymentRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PaymentsViewModel(paymentRepository, epRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}