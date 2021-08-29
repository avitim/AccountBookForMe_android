package com.example.accountbookforme.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.model.Name
import com.example.accountbookforme.database.repository.StoreRepository
import com.example.accountbookforme.util.RestUtil
import kotlinx.coroutines.launch

class StoresViewModel : ViewModel() {

    private val storeRepository: StoreRepository =
        RestUtil.retrofit.create(StoreRepository::class.java)

    // 店舗一覧
    var storeList: MutableLiveData<List<Filter>> = MutableLiveData()

    init {
        loadStoreList()
    }

    /**
     * 登録済み店舗一覧取得
     */
    private fun loadStoreList() {

        viewModelScope.launch {
            try {
                val request = storeRepository.findAll()
                if (request.isSuccessful) {
                    storeList.value = request.body()
                } else {
                    Log.e("StoresViewModel", "Not successful: $request")
                }
            } catch (e: Exception) {
                Log.e("StoresViewModel", "Something is wrong: $e")
            }
        }
    }

    /**
     * カテゴリ新規作成
     */
    fun create(name: Name) {

        viewModelScope.launch {
            try {
                val response = storeRepository.create(name)
                if (response.isSuccessful) {
                    storeList.value = response.body()
                } else {
                    Log.e("StoresViewModel", "Not successful: $response")
                }
            } catch (e: Exception) {
                Log.e("StoresViewModel", "Something is wrong: $e")
            }
        }
    }

    /**
     * カテゴリ更新
     */
    fun update(filter: Filter) {

        viewModelScope.launch {
            try {
                val response = storeRepository.update(filter)
                if (response.isSuccessful) {
                    storeList.value = response.body()
                } else {
                    Log.e("StoresViewModel", "Not successful: $response")
                }
            } catch (e: Exception) {
                Log.e("StoresViewModel", "Something is wrong: $e")
            }
        }
    }

    /**
     * カテゴリ削除
     */
    fun delete(id: Long) {

        viewModelScope.launch {
            try {
                val response = storeRepository.delete(id)
                if (response.isSuccessful) {
                    storeList.value = response.body()
                } else {
                    Log.e("StoresViewModel", "Not successful: $response")
                }
            } catch (e: Exception) {
                Log.e("StoresViewModel", "Something is wrong: $e")
            }
        }
    }
}