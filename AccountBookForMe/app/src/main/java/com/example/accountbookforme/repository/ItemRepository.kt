package com.example.accountbookforme.repository

import com.example.accountbookforme.model.Item
import com.example.accountbookforme.model.TotalEachFilter
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ItemRepository {

    @GET("/items/category/{id}")
    suspend fun getItemsByCategoryId(@Path("id") categoryId: Long): Response<List<Item>>

    @GET("/items/category/totals")
    suspend fun getTotalEachCategory(): Response<List<TotalEachFilter>>
}