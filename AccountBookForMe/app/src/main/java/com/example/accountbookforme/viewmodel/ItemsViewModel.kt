package com.example.accountbookforme.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.accountbookforme.database.entity.ItemEntity
import com.example.accountbookforme.database.repository.ItemRepository

class ItemsViewModel(private val repository: ItemRepository) : ViewModel() {

    // 品物リスト
    var itemList: MutableLiveData<List<ItemEntity>> = MutableLiveData()

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