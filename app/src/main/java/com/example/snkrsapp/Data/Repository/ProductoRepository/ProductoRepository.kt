package com.example.snkrsapp.Data.Repository.ProductoRepository

import com.example.snkrsapp.Data.RemoteData.ProductoDao.AgregarProductoSolicitud
import com.example.snkrsapp.Data.RemoteData.PublicacionDao.AgregarPublicacionesSolicitud
import com.example.snkrsapp.Domain.EstadoProductoNuevo
import com.example.snkrsapp.Domain.Marca
import com.example.snkrsapp.Domain.Producto
import com.example.snkrsapp.Domain.ProductoItem
import com.example.snkrsapp.Domain.Publicacion
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    suspend fun traerPaginaProductos(limite: Int, salto: Int): Flow<List<Producto>>
    suspend fun traerMarcas(): Flow<List<Marca>>
    suspend fun traerProductoPorId(id: Int): Flow<Producto>
    suspend fun traerMarcaPorId(id: Int): Flow<Marca>
    suspend fun traerPublicaciones(idProducto: Int): Flow<List<Publicacion>>
    suspend fun agregarPublicacion(
        body: AgregarPublicacionesSolicitud,
        token: String,
        uid : String
    ): Flow<EstadoProductoNuevo>

    suspend fun buscarSugerencias(token: String, idMarca: Int, busqueda: String): List<ProductoItem>
}