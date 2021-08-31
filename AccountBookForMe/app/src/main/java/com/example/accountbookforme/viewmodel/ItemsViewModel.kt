package com.example.accountbookforme.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.accountbookforme.database.entity.ItemEntity
import com.example.accountbookforme.database.repository.ItemRepository
import com.example.accountbookforme.model.Total

class ItemsViewModel(private val repository: ItemRepository) : ViewModel() {

    // 支出額リスト
    var totalList: MutableLiveData<List<Total>> = MutableLiveData()

    // 品物リスト
    var itemList: MutableLiveData<List<ItemEntity>> = MutableLiveData()

    /**
     * 支出額リスト取得
     */
    // TODO: 要実装
    suspend fun loadTotalList() = repository.getTotalCategoryList()

    /**
     * カテゴリIDから品物リスト取得
     */
    suspend fun findByCategoryId(categoryId: Long) {
        itemList.value = repository.findByCategoryId(categoryId)
    }
}

class ItemsViewModelFactory(private val repository: ItemRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}