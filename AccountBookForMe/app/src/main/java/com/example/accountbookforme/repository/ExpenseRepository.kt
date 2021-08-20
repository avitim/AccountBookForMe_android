package com.example.accountbookforme.repository

import com.example.accountbookforme.model.ExpenseDetail
import com.example.accountbookforme.model.Expense
import com.example.accountbookforme.model.TotalEachFilter
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ExpenseRepository {

    @GET("/expenses")
    suspend fun getAll(): Response<List<Expense>>

    @GET("/expenses/{id}")
    suspend fun getDetailById(@Path("id") expenseId: Long): Response<ExpenseDetail>

    @GET("/expenses/payment/totals")
    suspend fun getTotalPaymentList(): Response<List<TotalEachFilter>>

    @PUT("/expenses/create")
    suspend fun create(@Body expenseDetail: ExpenseDetail): Response<Void>

    @PUT("/expenses/update")
    suspend fun update(@Body expenseDetail: ExpenseDetail): Response<Void>

    @DELETE("/expenses/delete/{id}")
    suspend fun delete(@Path("id") expenseId: Long): Response<Void>
}