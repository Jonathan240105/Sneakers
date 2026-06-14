package com.example.snkrsapp.Data.RemoteData.PublicacionDao

import com.example.snkrsapp.Data.RemoteData.Variables.Endpoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PublicacionDao {

    @GET(Endpoints.ObtenerPublicacionesPorModelo)
    suspend fun obtenerPublicacionesPorModelo(
        @Query("modelo") idProducto: Int
    ): Response<List<PublicacionesRespuesta>>

    @POST(Endpoints.AgregarPublicacion)
    suspend fun agregarPublicacion(
        @Header("Authorization") token: String,
        @Body body: AgregarPublicacionesSolicitud
    ): Response<AgregarPublicacionRespuesta>

    @GET(Endpoints.ObtenerCarritoUsuario)
    suspend fun obtenerCarritoUsuario(
        @Header("Authorization") token: String
    ): Response<List<PublicacionPropiaListado>>

    @GET(Endpoints.ObtenerColeccionUsuario)
    suspend fun obtenerColeccionUsuario(
        @Header("Authorization") token: String,
        @Path("uid") uid: String
    ): Response<List<PublicacionCarritoRespuesta>>

    @GET(Endpoints.ObtenerVentasUsuario)
    suspend fun obtenerVentasUsuario(
        @Header("Authorization") token: String,
        @Path("uid") uid: String
    ): Response<List<PublicacionPropiaListado>>

    @GET(Endpoints.ObtenerPublicacionesPorProducto)
    suspend fun getPublicacionesPorProducto(
        @Path("idProducto") idProducto: Int,
        @Header("Authorization") token: String
    ): Response<List<PublicacionesProductoRespuesta>>

    @POST(Endpoints.AgregarAlCarrito)
    suspend fun agregarAlCarrito(
        @Header("Authorization") token: String,
        @Query("idPublicacion") idPublicacion: Int
    ): Response<AgregarACarritoRespuesta>

    @POST(Endpoints.ComprarCarrito)
    suspend fun comprarCarrito(
        @Header("Authorization") token: String
    ): Response<CompraRespuesta>

}