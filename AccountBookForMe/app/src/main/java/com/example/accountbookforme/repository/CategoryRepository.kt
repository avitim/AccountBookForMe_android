package com.example.accountbookforme.repository

import com.example.accountbookforme.model.Filter
import retrofit2.Response
import retrofit2.http.GET

interface CategoryRepository {

    @GET("/categories")
    suspend fun findAll(): Response<List<Filter>>

}