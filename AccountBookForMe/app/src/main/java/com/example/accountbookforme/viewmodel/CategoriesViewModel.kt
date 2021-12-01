package com.example.accountbookforme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.database.entity.CategoryEntity
import com.example.accountbookforme.database.repository.CategoryRepository
import com.example.accountbookforme.database.repository.ItemRepository
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.model.Total
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val categoryRepository: CategoryRepository,
    private val itemRepository: ItemRepository
) : ViewModel() {

    // カテゴリ一覧
    var categoryList: LiveData<List<CategoryEntity>> = categoryRepository.categoryList.asLiveData()

    // カテゴリごとの総額リスト
    private var totalList = MutableLiveData<List<Total>>()

    /**
     * カテゴリ一覧をFilter型のリストで取得
     */
    fun getCategoriesAsFilter(): List<Filter> = categoryList.value?.map { category ->
        Filter(category.id, category.name)
    }.orEmpty()

    /**
     * カテゴリごとの総額を取得
     */
    suspend fun loadTotalList() {

        totalList.value = categoryList.value?.map { category ->
            Total(
                id = category.id,
                name = category.name,
                total = itemRepository.getCategoryTotal(category.id)
            )
        }
    }

    /**
     * カテゴリごとの総額リストを返す
     */
    fun getTotalList(): List<Total> = totalList.value.orEmpty()

    /**
     * IDからカテゴリを取得
     */
    fun getById(id: Long) = categoryList.value?.find { category -> category.id == id }

    /**
     * カテゴリ新規作成
     */
    fun create(name: String) = viewModelScope.launch {
        categoryRepository.create(CategoryEntity(name = name))
    }

    /**
     * カテゴリ更新
     */
    fun update(filter: Filter) = viewModelScope.launch {
        filter.id?.let { CategoryEntity(id = it, name = filter.name) }
            ?.let { categoryRepository.update(it) }
    }

    /**
     * カテゴリ削除
     */
    fun deleteById(id: Long) = viewModelScope.launch {
        categoryRepository.deleteById(id)
    }
}

class CategoriesViewModelFactory(
    private val categoryRepository: CategoryRepository,
    private val itemRepository: ItemRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoriesViewModel(categoryRepository, itemRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}