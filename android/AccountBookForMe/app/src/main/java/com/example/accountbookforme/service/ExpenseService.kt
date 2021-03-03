package com.example.accountbookforme.service

import com.example.accountbookforme.model.Expense
import com.example.accountbookforme.model.ExpenseForm
import com.example.accountbookforme.model.ExpenseListItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ExpenseService {

    @GET("/api/v1/expenselist")
    fun getAllItems(): Call<List<ExpenseListItem>>

    @GET("/api/v1/expenses/{id}")
    fun getById(@Path("id") expenseId: Long): Call<ExpenseForm>

    @POST("api/v1/expenses")
    fun create(@Body expenseForm: ExpenseForm): Call<Expense>

    @PUT("/api/v1/expenses/{id}")
    fun update(@Path("id") expenseId: Long, @Body expenseForm: ExpenseForm): Call<Expense>

    @DELETE("/api/v1/expenses/{id}")
    fun delete(@Path("id") expenseId: Long)
}