package com.example.accountbookforme.service

import com.example.accountbookforme.model.ExpenseListItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ExpenseService {

    @GET("/api/v1/expenselist")
    fun getAll(): Call<List<ExpenseListItem>>

    @GET("/api/v1/expenses/{id}")
    fun getById(@Path("id") expenseId: Long): Call<ExpenseListItem>
}