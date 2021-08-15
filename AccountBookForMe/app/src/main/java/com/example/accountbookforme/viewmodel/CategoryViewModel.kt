package com.example.accountbookforme.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.repository.CategoryRepository
import com.example.accountbookforme.util.RestUtil
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {

    private val categoryRepository: CategoryRepository = RestUtil.retrofit.create(CategoryRepository::class.java)

    // 店舗一覧
    var categoryList: MutableLiveData<List<Filter>> = MutableLiveData()

    fun getCategoryList() {

        viewModelScope.launch {
            try {
                val request = categoryRepository.getList()
                if (request.isSuccessful) {
                    categoryList.value = request.body()
                } else {
                    Log.e("categoryList", "Something is wrong: $request")
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }

}