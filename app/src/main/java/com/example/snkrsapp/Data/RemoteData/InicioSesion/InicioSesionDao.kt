package com.example.snkrsapp.Data.RemoteData.InicioSesion

import com.example.snkrsapp.Data.RemoteData.Variables.Endpoints
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface InicioSesionDao {
    @POST(Endpoints.IniciarSesion)
    suspend fun iniciarSesion(
        @Header("Authorization") token: String?
    ): Response<InicioSesionRespuesta>
}