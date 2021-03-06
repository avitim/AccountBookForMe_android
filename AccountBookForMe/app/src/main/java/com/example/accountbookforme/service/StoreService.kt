package com.example.accountbookforme.service

import com.example.accountbookforme.model.Store
import retrofit2.Call
import retrofit2.http.GET

interface StoreService {

    @GET("/api/v1/stores")
    fun getListItems(): Call<List<Store>>

}