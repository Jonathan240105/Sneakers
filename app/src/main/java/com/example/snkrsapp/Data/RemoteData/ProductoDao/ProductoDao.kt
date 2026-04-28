package com.example.snkrsapp.Data.RemoteData.ProductoDao

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductoDao {

    @GET("/productos")
    suspend fun obtenerProductos(): Response<List<TodosProductosRespuesta>>

    @GET("/productos")
    suspend fun obtenerPaginaProductos(
        @Query("limit") limite: Int,
        @Query("offset") salto: Int
    ): Response<List<TodosProductosRespuesta>>
}