package com.example.snkrsapp.Data.RemoteData.PublicacionDao

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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
}