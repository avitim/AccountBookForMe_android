package com.example.accountbookforme.repository

import com.example.accountbookforme.model.Expense
import com.example.accountbookforme.service.ExpenseService
import com.example.accountbookforme.util.RestUtil
import retrofit2.Call

class ExpenseRepository {

    private var expenseService: ExpenseService = RestUtil.retrofit.create(ExpenseService::class.java)

    fun getAll(): Call<List<Expense>> = expenseService.getAll()

    fun getById(id: Long): Call<Expense> = expenseService.getById(id)

    // singletonでRepositoryインスタンスを返すFactory
    companion object Factory {
        val instance: ExpenseRepository
            @Synchronized get() {
                return ExpenseRepository()
            }
    }
}