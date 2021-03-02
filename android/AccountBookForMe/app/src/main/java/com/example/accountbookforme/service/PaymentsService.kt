package com.example.accountbookforme.service

import com.example.accountbookforme.model.PaymentListItem
import retrofit2.Call
import retrofit2.http.GET

interface PaymentsService {

    @GET("/api/v1/paymentlistitem")
    fun getListItems(): Call<List<PaymentListItem>>
}