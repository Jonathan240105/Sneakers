package com.example.snkrsapp.Data.Repository.ProductoRepository

import com.example.snkrsapp.Data.LocalData.Marcas.EntityToMarca
import com.example.snkrsapp.Data.LocalData.Marcas.MarcaEntity
import com.example.snkrsapp.Data.LocalData.Marcas.MarcaLocalDao
import com.example.snkrsapp.Data.LocalData.Marcas.MarcaRespuestaToEntity
import com.example.snkrsapp.Data.LocalData.Perfil.ColeccionEntity
import com.example.snkrsapp.Data.LocalData.Perfil.VentasEntity
import com.example.snkrsapp.Data.LocalData.Productos.ProductoEntity
import com.example.snkrsapp.Data.LocalData.Productos.ProductoEntityToProducto
import com.example.snkrsapp.Data.LocalData.Productos.ProductoLocalDao
import com.example.snkrsapp.Data.LocalData.Productos.ProductoRespuestaToProductoEntity
import com.example.snkrsapp.Data.LocalData.PublicacionPropia.PublicacionesPropiasLocalDao
import com.example.snkrsapp.Data.LocalData.Publicaciones.EntityToPublicacion
import com.example.snkrsapp.Data.LocalData.Publicaciones.PublicacionEntity
import com.example.snkrsapp.Data.LocalData.Publicaciones.PublicacionLocalDao
import com.example.snkrsapp.Data.LocalData.Publicaciones.PublicacionToEntity
import com.example.snkrsapp.Data.RemoteData.ProductoDao.AgregarProductoSolicitud
import com.example.snkrsapp.Data.RemoteData.ProductoDao.ProductosDao
import com.example.snkrsapp.Data.RemoteData.PublicacionDao.AgregarPublicacionesSolicitud
import com.example.snkrsapp.Data.RemoteData.PublicacionDao.PublicacionDao
import com.example.snkrsapp.Domain.EstadoProductoNuevo
import com.example.snkrsapp.Domain.Marca
import com.example.snkrsapp.Domain.Producto
import com.example.snkrsapp.Domain.ProductoItem
import com.example.snkrsapp.Domain.Publicacion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductoRepositoryImp @Inject constructor(
    private val productosDao: ProductosDao,
    private val productoLocalDao: ProductoLocalDao,
    private val marcaLocalDao: MarcaLocalDao,
    private val publicacionLocalDao: PublicacionLocalDao,
    private val publicacionDao: PublicacionDao,
    private val publicacionPropiaLocalDao: PublicacionesPropiasLocalDao
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

    override suspend fun traerPublicaciones(idProducto: Int): Flow<List<Publicacion>> = flow {

        val publicacionesLocales = publicacionLocalDao.getPublicaciones(idProducto)
        if (publicacionesLocales.isNotEmpty()) {
            emit(publicacionesLocales.map { EntityToPublicacion(it) })
            return@flow
        }

        try {
            val respuesta = publicacionDao.obtenerPublicacionesPorModelo(idProducto)
            if (respuesta.isSuccessful) {
                publicacionLocalDao.agregarPublicaciones(respuesta.body()?.map {
                    PublicacionToEntity(it)
                } ?: emptyList())
                emit(publicacionesLocales.map { EntityToPublicacion(it) })
            }
        } catch (e: Exception) {
            emit(emptyList())
            println("Error al obtener las publicaciones: ${e.message}")
        }
    }

    override suspend fun agregarPublicacion(
        body: AgregarPublicacionesSolicitud,
        token: String,
        uid: String,
        nombreMarcaSeleccionada: String,
        nombreModeloSeleccionado: String
    ): Flow<EstadoProductoNuevo> = flow {
        try {
            val respuesta = publicacionDao.agregarPublicacion("Bearer $token", body)

            if (respuesta.isSuccessful && respuesta.body()?.ok == true) {
                val datos = respuesta.body()!!

               var idMarcaFinal = body.idMarca
                if (body.esMarcaNueva && datos.idMarca != null) {
                    idMarcaFinal = datos.idMarca
                    marcaLocalDao.insertarListaMarcas(
                        listOf(
                            MarcaEntity(
                                idMarca = datos.idMarca,
                                nombre = body.nombreMarcaNueva ?: "Desconocido"
                            )
                        )
                    )
                }

                var idProductoFinal = body.idProducto
                if (body.esProductoNuevo && datos.idProducto != null) {
                    idProductoFinal = datos.idProducto
                    productoLocalDao.añadirProducto(
                        ProductoEntity(
                            idProducto = datos.idProducto,
                            idMarca = idMarcaFinal ?: 0,
                            modelo = body.nombreProductoNuevo ?: "",
                            precio = body.precio.toInt(),
                            talla = body.talla.toInt(),
                            uidVendedor = "SISTEMA",
                            imagenUrl = body.urlFoto
                        )
                    )
                }

                if (!body.esParaColeccion) {

                    if (datos.idPublicacion != null && idProductoFinal != null) {
                        publicacionLocalDao.agregarPublicaciones(
                            listOf(
                                PublicacionEntity(
                                    idPublicacion = datos.idPublicacion,
                                    idProducto = idProductoFinal,
                                    precio = body.precio,
                                    talla = body.talla,
                                    estado = body.estado,
                                    urlFoto = body.urlFoto,
                                    fechaPublicacion = body.fecha_publicacion,
                                    disponible = true,
                                    uidUsuario = uid
                                )
                            )
                        )

                        publicacionPropiaLocalDao.insertarVentasLocal(
                            listOf(
                                VentasEntity(
                                    uidUsuario = uid,
                                    idPublicacion = datos.idPublicacion,
                                    idProducto = idProductoFinal,
                                    idMarca = idMarcaFinal ?: 0,
                                    modelo = if (body.esProductoNuevo) body.nombreProductoNuevo ?: "" else nombreModeloSeleccionado,
                                    marca = if (body.esMarcaNueva) body.nombreMarcaNueva ?: "" else nombreMarcaSeleccionada,
                                    precio = body.precio,
                                    talla = body.talla,
                                    urlFoto = body.urlFoto,
                                    estado = body.estado
                                )
                            )
                        )
                    }

                } else {

                    if (idProductoFinal != null) {
                        publicacionPropiaLocalDao.insertarColeccionLocal(
                            listOf(
                                ColeccionEntity(
                                    uidUsuario = uid,
                                    idProducto = idProductoFinal,
                                    idMarca = idMarcaFinal ?: 0,
                                    modelo = if (body.esProductoNuevo) body.nombreProductoNuevo
                                        ?: "" else nombreModeloSeleccionado,
                                    marca = if (body.esMarcaNueva) body.nombreMarcaNueva
                                        ?: "" else nombreMarcaSeleccionada,
                                    urlFoto = body.urlFoto,
                                    precio = body.precio
                                )
                            )
                        )
                    }
                }

                emit(EstadoProductoNuevo(true, datos.message))

            } else {
                val msg = respuesta.body()?.message ?: "Error al procesar la publicación"
                emit(EstadoProductoNuevo(false, msg))
            }
        } catch (e: Exception) {
            emit(EstadoProductoNuevo(false, "Error de red: ${e.message}"))
        }
    }


    override suspend fun buscarSugerencias(
        token: String, idMarca: Int, busqueda: String
    ): List<ProductoItem> {
        try {
            val respuesta =
                productosDao.buscarSugerenciasModelo("Bearer $token", idMarca, busqueda)
            if (respuesta.isSuccessful && respuesta.body() != null) {
                println("Encontrados : ${respuesta.body()}")
                return respuesta.body()!!.map {
                    ProductoItem(idProducto = it.idProducto, modelo = it.modelo)
                }
            } else {
                return emptyList()
            }
        } catch (e: Exception) {
            println("Error al buscar sugerencias: ${e.message}")
            return emptyList()
        }
    }
}

