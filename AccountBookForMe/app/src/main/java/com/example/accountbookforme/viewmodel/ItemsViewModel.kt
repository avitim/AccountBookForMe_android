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
                val request = itemRepository.getTotalCategoryList()
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
    fun findByCategoryId(categoryId: Long) {

        viewModelScope.launch {
            try {
                val request = itemRepository.findByCategoryId(categoryId)
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
}