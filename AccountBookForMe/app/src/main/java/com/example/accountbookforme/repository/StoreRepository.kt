package com.example.accountbookforme.repository

import com.example.accountbookforme.model.Filter
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface StoreRepository {

    @GET("/stores")
    suspend fun getList(): Response<List<Filter>>

}