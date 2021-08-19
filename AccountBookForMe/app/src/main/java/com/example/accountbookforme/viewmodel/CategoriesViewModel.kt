package com.example.accountbookforme.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.model.TotalEachFilter
import com.example.accountbookforme.repository.ItemRepository
import com.example.accountbookforme.util.RestUtil
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CategoriesViewModel : ViewModel() {

    private val itemRepository: ItemRepository =
        RestUtil.retrofit.create(ItemRepository::class.java)

    // 支出額リスト
    var totalList: MutableLiveData<List<TotalEachFilter>> = MutableLiveData()

    init {
        loadTotalList()
    }

    /**
     * 支出額リスト取得
     */
    private fun loadTotalList() {

        viewModelScope.launch {
            try {
                val request = itemRepository.getTotalEachCategory()
                if (request.isSuccessful) {
                    totalList.value = request.body()
                } else {
                    Log.e("CategoriesViewModel", "Not successful: $request")
                }
            } catch (e: Exception) {
                Log.e("CategoriesViewModel", "Something is wrong: $e")
            }
        }
    }

    /**
     * 総額を計算
     */
    fun calcTotal(): BigDecimal? = totalList.value?.fold(BigDecimal.ZERO) { acc, total ->
        acc + total.total
    }
}