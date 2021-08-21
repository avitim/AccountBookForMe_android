package com.example.accountbookforme.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.model.Filter
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

    private fun loadCategoryList() {

        viewModelScope.launch {
            try {
                val request = categoryRepository.findAll()
                if (request.isSuccessful) {
                    categoryList.value = request.body()
                } else {
                    Log.e("categoryList", "Not successful: $request")
                }
            } catch (e: Exception) {
                Log.e("categoryList", "Something is wrong: $e")
            }
        }
    }

    // IDから名称を取得
    fun getNameById(id: Long): String {

        val category = categoryList.value?.find { category ->
            category.id == id
        }
        return category?.name ?: "Invalid category"
    }
}