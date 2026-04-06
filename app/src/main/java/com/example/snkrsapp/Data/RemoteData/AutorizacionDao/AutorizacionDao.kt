package com.example.snkrsapp.Data.RemoteData.AutorizacionDao

import com.example.snkrsapp.Data.RemoteData.Variables.Endpoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AutorizacionDao {
    @POST(Endpoints.IniciarSesion)
    suspend fun iniciarSesion(
        @Header("Authorization") token: String?
    ): Response<InicioSesionRespuesta>

    @POST(Endpoints.Registro)
    suspend fun registrarUsuario(
        @Body usuario: UsuarioSolicitud
    ): Response<RegistroRespuesta>
}