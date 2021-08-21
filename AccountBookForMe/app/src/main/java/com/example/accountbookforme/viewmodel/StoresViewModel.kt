package com.example.accountbookforme.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.repository.StoreRepository
import com.example.accountbookforme.util.RestUtil
import kotlinx.coroutines.launch

class StoresViewModel : ViewModel() {

    private val storeRepository: StoreRepository =
        RestUtil.retrofit.create(StoreRepository::class.java)

    // 店舗一覧
    var storeList: MutableLiveData<List<Filter>> = MutableLiveData()

    init {
        getStoreList()
    }

    private fun getStoreList() {

        viewModelScope.launch {
            try {
                val request = storeRepository.getAll()
                if (request.isSuccessful) {
                    storeList.value = request.body()
                } else {
                    Log.e("storeList", "Not successful: $request")
                }
            } catch (e: Exception) {
                Log.e("storeList", "Something is wrong: $e")
            }
        }
    }
}