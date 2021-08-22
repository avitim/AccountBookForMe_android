package com.example.accountbookforme.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.model.Name
import com.example.accountbookforme.repository.PaymentRepository
import com.example.accountbookforme.util.RestUtil
import kotlinx.coroutines.launch

class PaymentsViewModel : ViewModel() {

    private val paymentRepository: PaymentRepository =
        RestUtil.retrofit.create(PaymentRepository::class.java)

    // 決済方法一覧
    var paymentList: MutableLiveData<List<Filter>> = MutableLiveData()

    init {
        loadPaymentList()
    }

    /**
     *  決済方法一覧取得
     */
    private fun loadPaymentList() {

        viewModelScope.launch {
            try {
                val request = paymentRepository.findAll()
                if (request.isSuccessful) {
                    paymentList.value = request.body()
                } else {
                    Log.e("PaymentsViewModel", "Not successful: $request")
                }
            } catch (e: Exception) {
                Log.e("PaymentsViewModel", "Something is wrong: $e")
            }
        }
    }

    /**
     * IDから名称を取得
     */
    fun getNameById(id: Long): String {

        val payment = paymentList.value?.find { payment ->
            payment.id == id
        }
        return payment?.name ?: "Invalid payment method"
    }

    /**
     * カテゴリ新規作成
     */
    fun create(name: Name) {

        viewModelScope.launch {
            try {
                val response = paymentRepository.create(name)
                if (response.isSuccessful) {
                    paymentList.value = response.body()
                } else {
                    Log.e("PaymentsViewModel", "Not successful: $response")
                }
            } catch (e: Exception) {
                Log.e("PaymentsViewModel", "Something is wrong: $e")
            }
        }
    }

    /**
     * カテゴリ更新
     */
    fun update(filter: Filter) {

        viewModelScope.launch {
            try {
                val response = paymentRepository.update(filter)
                if (response.isSuccessful) {
                    paymentList.value = response.body()
                } else {
                    Log.e("PaymentsViewModel", "Not successful: $response")
                }
            } catch (e: Exception) {
                Log.e("PaymentsViewModel", "Something is wrong: $e")
            }
        }
    }

    /**
     * カテゴリ削除
     */
    fun delete(id: Long) {

        viewModelScope.launch {
            try {
                val response = paymentRepository.delete(id)
                if (response.isSuccessful) {
                    paymentList.value = response.body()
                } else {
                    Log.e("PaymentsViewModel", "Not successful: $response")
                }
            } catch (e: Exception) {
                Log.e("PaymentsViewModel", "Something is wrong: $e")
            }
        }
    }
}