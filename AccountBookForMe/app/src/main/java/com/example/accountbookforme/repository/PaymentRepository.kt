package com.example.accountbookforme.repository

import com.example.accountbookforme.model.PaymentListItem
import retrofit2.Call
import retrofit2.http.GET

interface PaymentRepository {

    @GET("/api/v1/paymentlistitem")
    fun getListItems(): Call<List<PaymentListItem>>
}