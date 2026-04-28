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
    private val productoDao: ProductoDao, private val productoLocalDao: ProductoLocalDao
) : ProductoRepository {

    override suspend fun traerPaginaProductos(limite: Int, salto: Int): Flow<List<Producto>> =
        flow {
            val localesIniciales = productoLocalDao.obtenerPaginaProductos(limite, salto)
            if (localesIniciales.isNotEmpty()) {
                emit(localesIniciales.map { ProductoEntityToProducto(it) })
                return@flow
            }
            try {
                val respuesta = productoDao.obtenerPaginaProductos(limite, salto)
                if (respuesta.isSuccessful && respuesta.body() != null) {
                    productoLocalDao.insertarLista(respuesta.body()?.map {
                        ProductoRespuestaToProductoEntity(it)
                    } ?: emptyList())

                    val localesActualizados = productoLocalDao.obtenerPaginaProductos(limite, salto)
                    emit(localesActualizados.map { ProductoEntityToProducto(it) })
                }
            } catch (e: Exception) {
            }
        }

}