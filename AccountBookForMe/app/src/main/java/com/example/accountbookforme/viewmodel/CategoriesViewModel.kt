package com.example.accountbookforme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.entity.CategoryEntity
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.repository.CategoryRepository
import kotlinx.coroutines.launch

class CategoriesViewModel(private val repository: CategoryRepository) : ViewModel() {

    // カテゴリ一覧
    var categoryList: LiveData<List<CategoryEntity>> = repository.categoryList.asLiveData()

    /**
     * IDからカテゴリを取得
     */
    fun getById(id: Long) = categoryList.value?.find { category -> category.id == id}

    /**
     * カテゴリ一覧をFilter型のリストで取得
     */
    fun getCategoriesAsFilter(): List<Filter> {
        val filterList: MutableList<Filter> = arrayListOf()
        categoryList.value?.forEach { category ->
            filterList.add(Filter(category.id, category.name))
        }
        return filterList
    }

    /**
     * カテゴリ新規作成
     */
    fun create(name: String) = viewModelScope.launch {
        repository.create(CategoryEntity(name = name))
    }

    /**
     * カテゴリ更新
     */
    fun update(filter: Filter) = viewModelScope.launch {
        filter.id?.let { CategoryEntity(id = it, name = filter.name) }?.let { repository.update(it) }
    }

    /**
     * カテゴリ削除
     */
    fun deleteById(id: Long) = viewModelScope.launch {
        repository.deleteById(id)
    }
}

class CategoriesViewModelFactory(private val repository: CategoryRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoriesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}