package com.example.accountbookforme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.database.entity.StoreEntity
import com.example.accountbookforme.database.repository.StoreRepository
import com.example.accountbookforme.model.Filter
import kotlinx.coroutines.launch

class StoresViewModel(private val repository: StoreRepository) : ViewModel() {

    // 店舗一覧
    var storeList: LiveData<List<StoreEntity>> = repository.storeList.asLiveData()

    /**
     * カテゴリ一覧をFilter型のリストで取得
     */
    fun getStoresAsFilter(): List<Filter> {
        val filterList: MutableList<Filter> = arrayListOf()
        storeList.value?.forEach { store ->
            filterList.add(Filter(store.id, store.name))
        }
        return filterList
    }

    /**
     * 店舗新規作成
     */
    fun create(name: String) = viewModelScope.launch {
        repository.create(StoreEntity(name = name))
    }

    /**
     * 店舗更新
     */
    fun update(filter: Filter) = viewModelScope.launch {
        filter.id?.let { StoreEntity(id = it, name = filter.name) }?.let { repository.update(it) }
    }

    /**
     * 店舗削除
     */
    fun deleteById(id: Long) = viewModelScope.launch {
        repository.deleteById(id)
    }
}

class StoresViewModelFactory(private val repository: StoreRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoresViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoresViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}