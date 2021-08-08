package com.example.accountbookforme.repository

import com.example.accountbookforme.model.Store
import retrofit2.Call
import retrofit2.http.GET

interface StoreRepository {

    @GET("/api/v1/stores")
    fun getListItems(): Call<List<Store>>

}