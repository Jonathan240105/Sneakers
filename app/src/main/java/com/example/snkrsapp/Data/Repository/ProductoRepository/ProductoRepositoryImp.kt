package com.example.snkrsapp.Data.Repository.ProductoRepository

import com.example.snkrsapp.Data.LocalData.Productos.ProductoEntityToProducto
import com.example.snkrsapp.Data.LocalData.Productos.ProductoLocalDao
import com.example.snkrsapp.Data.LocalData.Productos.ProductoRespuestaToProductoEntity
import com.example.snkrsapp.Data.RemoteData.ProductoDao.ProductoDao
import com.example.snkrsapp.Domain.Producto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductoRepositoryImp @Inject constructor(
    private val productoDao: ProductoDao,
    private val productoLocalDao: ProductoLocalDao
) : ProductoRepository {
    override suspend fun obtenerProductos(): Flow<List<Producto>> = flow {

        val productosLocales = productoLocalDao.obtenerProductos()

        if (productosLocales.isEmpty()) {
            emit(productosLocales.map { ProductoEntityToProducto(it) })
        }

        try {
            val respuesta =
                productoDao.obtenerProductos()

            if (respuesta.isSuccessful && respuesta.body() != null) {
                val productosRemotos = respuesta.body()

                productoLocalDao.insertarLista(productosRemotos?.map {
                    ProductoRespuestaToProductoEntity(
                        it
                    )
                } ?: emptyList())

                val productosLocalesActualizados = productoLocalDao.obtenerProductos()
                emit(productosLocalesActualizados.map { ProductoEntityToProducto(it) })
            }
        } catch (e: Exception) {
            println("Error de red, manteniendo caché: ${e.message}")
        }
    }

}