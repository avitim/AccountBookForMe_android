package com.example.accountbookforme.service

import com.example.accountbookforme.model.Expense
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ExpenseService {

    @GET("expenses")
    fun getAll(): Call<List<Expense>>

    @GET("expenses/{id}")
    fun getById(@Path("id") expenseId: Long): Call<Expense>
}