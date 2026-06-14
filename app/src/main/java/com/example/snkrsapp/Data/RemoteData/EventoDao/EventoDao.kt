package com.example.snkrsapp.Data.RemoteData.EventoDao

import com.example.snkrsapp.Data.RemoteData.Variables.Endpoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface EventoDao {

    @GET(Endpoints.GetEventos)
    suspend fun getEventos(
        @Header("Authorization") token: String,
        @Query("fecha") fecha: String
    ): Response<List<EventosRespuesta>>

    @GET(Endpoints.EliminarEvento)
    suspend fun eliminarEvento(
        @Header("Authorization") token: String,
        @Path("id") idEvento: Int
    ): Response<EventosRespuestaSimple>

    @POST(Endpoints.CrearEvento)
    suspend fun crearEvento(
        @Header("Authorization") token: String,
        @Body nuevoEvento: EventosSolicitud
    ): Response<EventosRespuestaSimple>

    @GET(Endpoints.GetEventosAdmin)
    suspend fun obtenerEventosAdmin(
        @Header("Authorization") token: String
    ): Response<List<EventosRespuesta>>

    @HTTP(method = "DELETE", path = Endpoints.EliminarEventos, hasBody = true)
    suspend fun eliminarEventos(
        @Header("Authorization") token: String,
        @Body request: EliminarEventoSolicitud
    ): Response<EliminarEventosRespuesta>

    @POST(Endpoints.CrearEventoAdmin)
    suspend fun crearEventoGLobal(
        @Header("Authorization") token: String,
        @Body body: CrearEventoSolicitud
    ): Response<CrearEventoRespuesta>
}