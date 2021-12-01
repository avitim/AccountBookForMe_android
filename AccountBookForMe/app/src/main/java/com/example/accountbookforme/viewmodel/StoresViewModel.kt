package com.example.accountbookforme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.database.entity.StoreEntity
import com.example.accountbookforme.database.repository.ExpensePaymentRepository
import com.example.accountbookforme.database.repository.ExpenseRepository
import com.example.accountbookforme.database.repository.StoreRepository
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.model.Total
import kotlinx.coroutines.launch
import java.math.BigDecimal

class StoresViewModel(
    private val storeRepository: StoreRepository,
    private val expenseRepository: ExpenseRepository,
    private val epRepository: ExpensePaymentRepository
) : ViewModel() {

    // 店舗一覧
    var storeList: LiveData<List<StoreEntity>> = storeRepository.storeList.asLiveData()

    // 店舗ごとの総額リスト
    private var totalList = MutableLiveData<List<Total>>()

    /**
     * カテゴリ一覧をFilter型のリストで取得
     */
    fun getStoresAsFilter(): List<Filter> =
        storeList.value?.map { store -> Filter(store.id, store.name) }.orEmpty()

    /**
     * 店舗IDをもとにStoreEntityを取得する
     */
    suspend fun findById(id: Long): StoreEntity = storeRepository.findById(id)

    /**
     * 店舗ごとの総額を取得（店舗一覧にない場合はその他としてまとめる）
     */
    suspend fun loadTotalList() {

        // その他枠を追加
        val allStoreList = storeList.value?.plus(StoreEntity(name = "その他"))
        totalList.value = allStoreList?.map { store ->
            Total(
                id = store.id,
                name = store.name,
                total = calcTotalByStore(store.id)
            )
        }
    }

    /**
     * 店舗ごとの総額リストを返す
     */
    fun getTotalList(): List<Total> = totalList.value.orEmpty()

    /**
     * 店舗IDから総額を取得
     */
    private suspend fun calcTotalByStore(storeId: Long): BigDecimal =
        // storeIdが0ならnullで置き換える
        expenseRepository.findByStoreId(
            if (storeId == 0L) {
                null
            } else {
                storeId
            }
        ).fold(BigDecimal.ZERO) { acc, expense ->
            acc + epRepository.calcTotalByExpenseId(expense.id)
        }

    /**
     * 店舗新規作成
     */
    fun create(name: String) = viewModelScope.launch {
        storeRepository.create(StoreEntity(name = name))
    }

    /**
     * 店舗更新
     */
    fun update(filter: Filter) = viewModelScope.launch {
        filter.id?.let { StoreEntity(id = it, name = filter.name) }
            ?.let { storeRepository.update(it) }
    }

    /**
     * 店舗削除
     */
    fun deleteById(id: Long) = viewModelScope.launch {
        storeRepository.deleteById(id)
    }
}

class StoresViewModelFactory(
    private val storeRepository: StoreRepository,
    private val expenseRepository: ExpenseRepository,
    private val epRepository: ExpensePaymentRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoresViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoresViewModel(storeRepository, expenseRepository, epRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}