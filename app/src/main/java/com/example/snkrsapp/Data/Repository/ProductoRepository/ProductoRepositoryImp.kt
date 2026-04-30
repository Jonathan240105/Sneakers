package com.example.snkrsapp.Data.Repository.ProductoRepository

import com.example.snkrsapp.Data.LocalData.Marcas.EntityToMarca
import com.example.snkrsapp.Data.LocalData.Marcas.MarcaLocalDao
import com.example.snkrsapp.Data.LocalData.Marcas.MarcaRespuestaToEntity
import com.example.snkrsapp.Data.LocalData.Productos.ProductoEntityToProducto
import com.example.snkrsapp.Data.LocalData.Productos.ProductoLocalDao
import com.example.snkrsapp.Data.LocalData.Productos.ProductoRespuestaToProductoEntity
import com.example.snkrsapp.Data.RemoteData.ProductoDao.ProductosDao
import com.example.snkrsapp.Domain.Marca
import com.example.snkrsapp.Domain.Producto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductoRepositoryImp @Inject constructor(
    private val productosDao: ProductosDao,
    private val productoLocalDao: ProductoLocalDao,
    private val marcaLocalDao: MarcaLocalDao
) : ProductoRepository {

    override suspend fun traerPaginaProductos(limite: Int, salto: Int): Flow<List<Producto>> =
        flow {
            val localesIniciales = productoLocalDao.obtenerPaginaProductos(limite, salto)
            if (localesIniciales.isNotEmpty()) {
                emit(localesIniciales.map { ProductoEntityToProducto(it) })
                return@flow
            }
            try {
                val respuesta = productosDao.obtenerPaginaProductos(limite, salto)
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

    override suspend fun traerMarcas(): Flow<List<Marca>> = flow {
        val marcasLocales = marcaLocalDao.obtenerMarcas()

        if (marcasLocales.isNotEmpty()) {
            emit(marcasLocales.map { EntityToMarca(it) })
            return@flow
        }

        try {
            val respuesta = productosDao.obtenerMarcas()

            if (respuesta.isSuccessful && respuesta.body() != null) {

                val entidadesParaGuardar = respuesta.body()?.map {
                    MarcaRespuestaToEntity(it)
                } ?: emptyList()

                marcaLocalDao.insertarListaMarcas(entidadesParaGuardar)


                val localesActualizados = marcaLocalDao.obtenerMarcas()
                emit(localesActualizados.map { EntityToMarca(it) })
                println(localesActualizados)
            }
        } catch (e: Exception) {
            emit(emptyList())
            println("Error al obtener las marcas: ${e.message}")
        }
    }

    override suspend fun traerProductoPorId(id: Int): Flow<Producto> = flow {

        val productoLocal = productoLocalDao.obtenerProductoPorId(id)
        if (productoLocal != null) {
            emit(ProductoEntityToProducto(productoLocal))
        }
    }

    override suspend fun traerMarcaPorId(id: Int): Flow<Marca> = flow {
        val marcaLocal = marcaLocalDao.obtenerMarcaPorId(id)

        if (marcaLocal != null) {
            emit(EntityToMarca(marcaLocal))
        }
    }
}
