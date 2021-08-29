package com.example.accountbookforme.database.repository

import com.example.accountbookforme.model.Item
import com.example.accountbookforme.model.Total
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ItemRepository {

    @GET("/items/category/{id}")
    suspend fun findByCategoryId(@Path("id") categoryId: Long): Response<List<Item>>

    @GET("/items/category/totals")
    suspend fun getTotalCategoryList(): Response<List<Total>>
}