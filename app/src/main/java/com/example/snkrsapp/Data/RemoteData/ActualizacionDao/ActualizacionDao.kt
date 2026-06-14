package com.example.snkrsapp.Data.RemoteData.ActualizacionDao

import com.example.snkrsapp.Data.RemoteData.Variables.Endpoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH

interface ActualizacionDao {
    @PATCH(Endpoints.ActualizarPerfil)
    suspend fun actualizarPerfil(
        @Header("Authorization") token: String,
        @Body request: ActualizarPerfilSolicitud
    ): Response<ActualizarPerfilRespuesta>
}