package com.example.snkrsapp.Data.RemoteData.PublicacionDao

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PublicacionDao {

    @GET("/publicaciones/modelo")
    suspend fun obtenerPublicacionesPorModelo(
        @Query("modelo") idProducto: Int
    ): Response<List<PublicacionesRespuesta>>

    @POST("publicaciones/nuevo")
    suspend fun agregarPublicacion(
        @Header("Authorization") token: String,
        @Body body: AgregarPublicacionesSolicitud
    ): Response<AgregarPublicacionRespuesta>

    @GET("/usuarios/perfil/carrito")
    suspend fun obtenerCarritoUsuario(
        @Header("Authorization") token: String
    ): Response<List<PublicacionPropiaListado>>

    @GET("/usuarios/{uid}/coleccion")
    suspend fun obtenerColeccionUsuario(
        @Header("Authorization") token: String,
        @Path("uid") uid: String
    ): Response<List<PublicacionCarritoRespuesta>>

    @GET("/usuarios/{uid}/ventas")
    suspend fun obtenerVentasUsuario(
        @Header("Authorization") token: String,
        @Path("uid") uid: String
    ): Response<List<PublicacionPropiaListado>>

    @GET("/publicaciones/producto/{idProducto}")
    suspend fun getPublicacionesPorProducto(
        @Path("idProducto") idProducto: Int,
        @Header("Authorization") token: String
    ): Response<List<PublicacionesProductoRespuesta>>

    @POST("usuarios/perfil/carrito/agregar")
    suspend fun agregarAlCarrito(
        @Header("Authorization") token: String,
        @Query("idPublicacion") idPublicacion: Int
    ): Response<AgregarACarritoRespuesta>

    @POST("/publicaciones/comprar")
    suspend fun comprarCarrito(
        @Header("Authorization") token: String
    ): Response<CompraRespuesta>

}