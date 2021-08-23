package com.example.accountbookforme.repository

import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.model.Name
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface PaymentRepository {

    @GET("/payments")
    suspend fun findAll(): Response<List<Filter>>

    @PUT("/payments/create")
    suspend fun create(@Body name: Name): Response<List<Filter>>

    @PUT("/payments/update")
    suspend fun update(@Body filter: Filter): Response<List<Filter>>

    @DELETE("/payments/delete/{id}")
    suspend fun delete(@Path("id") id: Long): Response<List<Filter>>
}