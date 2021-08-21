package com.example.accountbookforme.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.model.Item
import com.example.accountbookforme.model.Total
import com.example.accountbookforme.repository.ItemRepository
import com.example.accountbookforme.util.RestUtil
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ItemsViewModel : ViewModel() {

    private val itemRepository: ItemRepository =
        RestUtil.retrofit.create(ItemRepository::class.java)

    // 支出額リスト
    var totalList: MutableLiveData<List<Total>> = MutableLiveData()

    // 品物リスト
    var itemList: MutableLiveData<List<Item>> = MutableLiveData()

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
     * カテゴリIDから品物リスト取得
     */
    fun getItemListByCategoryId(categoryId: Long) {

        viewModelScope.launch {
            try {
                val request = itemRepository.getItemsByCategoryId(categoryId)
                if (request.isSuccessful) {
                    itemList.value = request.body()
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

    /**
     * 品物の総額を計算
     */
    fun calcItemTotal(): BigDecimal? = itemList.value?.fold(BigDecimal.ZERO) { acc, item ->
        acc + item.price
    }
}