package com.example.accountbookforme.repository

import com.example.accountbookforme.model.Account
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AccountRepository {

    @GET("users/{id}")
    suspend fun findById(@Path("id") id: String): Response<Account>
}