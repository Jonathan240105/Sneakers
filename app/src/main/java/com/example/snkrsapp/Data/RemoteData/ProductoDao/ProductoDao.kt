package com.example.snkrsapp.Data.RemoteData.ProductoDao

import retrofit2.Response
import retrofit2.http.GET

interface ProductoDao {

    @GET("/productos")
    suspend fun obtenerProductos(): Response<List<TodosProductosRespuesta>>
}