package com.example.snkrsapp.Data.RemoteData.ProductoDao

import com.example.snkrsapp.Data.RemoteData.PublicacionDao.SugerenciaProductoRespuesta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductosDao {

    @GET("/productos")
    suspend fun obtenerProductos(): Response<List<TodosProductosRespuesta>>

    @GET("/productos")
    suspend fun obtenerPaginaProductos(
        @Query("limit") limite: Int,
        @Query("offset") salto: Int
    ): Response<List<TodosProductosRespuesta>>

    @GET("/productos/filtrar")
    suspend fun obtenerPaginaProductosFiltrados(
        @Header("Authorization") token: String,
        @Query("minPrecio") minPrecio: Double?,
        @Query("maxPrecio") maxPrecio: Double?,
        @Query("talla") talla: Double?,
        @Query("marcas") marcas: String?,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<List<TodosProductosRespuesta>>

    @GET("productos/buscadorPrincipal")
    suspend fun buscarProductosGlobal(
        @Header("Authorization") token: String,
        @Query("busqueda") busqueda: String
    ): Response<List<TodosProductosRespuesta>>

    @GET("/marca")
    suspend fun obtenerMarcas(): Response<List<MarcaRespuesta>>

    @POST("/productos/nuevo")
    suspend fun añadirProducto(
        @Header("Authorization") token: String,
        @Body body: AgregarProductoSolicitud
    ): Response<AgregarProductoRespuesta>

    @GET("productos/sugerencias")
    suspend fun buscarSugerenciasModelo(
        @Header("Authorization") token: String,
        @Query("idMarca") idMarca: Int,
        @Query("modeloBuscado") modeloBuscado: String
    ): Response<List<SugerenciaProductoRespuesta>>
}