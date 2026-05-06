package com.example.snkrsapp.Data.RemoteData.EventoDao

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface EventoDao {

    @GET("/eventos")
    suspend fun getEventos(
        @Header("Authorization") token: String
    ): Response<EventosRespuesta>

    @GET("/eventos/eliminar/{id}")
    suspend fun eliminarEvento(
        @Header("Authorization") token: String,
        @Path("id") idEvento: Int
    ): Response<EventosRespuestaSimple>

    @POST("/eventos/nuevo")
    suspend fun crearEvento(
        @Header("Authorization") token: String,
        @Body nuevoEvento: EventosSolicitud
    ): Response<EventosRespuestaSimple>
}