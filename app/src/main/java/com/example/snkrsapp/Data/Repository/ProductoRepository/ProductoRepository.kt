package com.example.snkrsapp.Data.Repository.ProductoRepository

import com.example.snkrsapp.Domain.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    suspend fun obtenerProductos(): Flow<List<Producto>>
}