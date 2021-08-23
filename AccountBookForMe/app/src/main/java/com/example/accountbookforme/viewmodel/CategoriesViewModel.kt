package com.example.accountbookforme.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.model.Name
import com.example.accountbookforme.repository.CategoryRepository
import com.example.accountbookforme.util.RestUtil
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {

    private val categoryRepository: CategoryRepository =
        RestUtil.retrofit.create(CategoryRepository::class.java)

    // カテゴリ一覧
    var categoryList: MutableLiveData<List<Filter>> = MutableLiveData()

    init {
        loadCategoryList()
    }

    /**
     * カテゴリ一覧取得
     */
    private fun loadCategoryList() {

        viewModelScope.launch {
            try {
                val request = categoryRepository.findAll()
                if (request.isSuccessful) {
                    categoryList.value = request.body()
                } else {
                    Log.e("CategoriesViewModel", "Not successful: $request")
                }
            } catch (e: Exception) {
                Log.e("CategoriesViewModel", "Something is wrong: $e")
            }
        }
    }

    /**
     * IDから名称を取得
     */
    fun getNameById(id: Long): String {

        val category = categoryList.value?.find { category ->
            category.id == id
        }
        return category?.name ?: "Invalid category"
    }

    /**
     * カテゴリ新規作成
     */
    fun create(name: Name) {

        viewModelScope.launch {
            try {
                val response = categoryRepository.create(name)
                if (response.isSuccessful) {
                    categoryList.value = response.body()
                } else {
                    Log.e("CategoriesViewModel", "Not successful: $response")
                }
            } catch (e: Exception) {
                Log.e("CategoriesViewModel", "Something is wrong: $e")
            }
        }
    }

    /**
     * カテゴリ更新
     */
    fun update(filter: Filter) {

        viewModelScope.launch {
            try {
                val response = categoryRepository.update(filter)
                if (response.isSuccessful) {
                    categoryList.value = response.body()
                } else {
                    Log.e("CategoriesViewModel", "Not successful: $response")
                }
            } catch (e: Exception) {
                Log.e("CategoriesViewModel", "Something is wrong: $e")
            }
        }
    }

    /**
     * カテゴリ削除
     */
    fun delete(id: Long) {

        viewModelScope.launch {
            try {
                val response = categoryRepository.delete(id)
                if (response.isSuccessful) {
                    categoryList.value = response.body()
                } else {
                    Log.e("CategoriesViewModel", "Not successful: $response")
                }
            } catch (e: Exception) {
                Log.e("CategoriesViewModel", "Something is wrong: $e")
            }
        }
    }
}