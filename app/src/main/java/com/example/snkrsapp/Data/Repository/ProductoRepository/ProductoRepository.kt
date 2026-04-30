package com.example.snkrsapp.Data.Repository.ProductoRepository

import com.example.snkrsapp.Domain.Marca
import com.example.snkrsapp.Domain.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    suspend fun traerPaginaProductos(limite: Int, salto: Int): Flow<List<Producto>>
    suspend fun traerMarcas(): Flow<List<Marca>>
    suspend fun traerProductoPorId(id: Int): Flow<Producto>
    suspend fun traerMarcaPorId(id: Int): Flow<Marca>
}