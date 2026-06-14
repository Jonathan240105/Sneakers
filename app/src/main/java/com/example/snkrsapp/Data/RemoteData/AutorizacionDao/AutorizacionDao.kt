package com.example.snkrsapp.Data.RemoteData.AutorizacionDao

import com.example.snkrsapp.Data.RemoteData.Variables.Endpoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AutorizacionDao {
    @POST(Endpoints.IniciarSesion)
    suspend fun iniciarSesion(
        @Header("Authorization") token: String?
    ): Response<InicioSesionRespuesta>

    @POST(Endpoints.Registro)
    suspend fun registrarUsuario(
        @Body usuario: UsuarioSolicitud
    ): Response<RegistroRespuesta>

    @GET(Endpoints.GetPerfil)
    suspend fun getPerfil(
        @Header("Authorization") token: String?,
        @Query("uid") uid: String?
    ): Response<Usuario>

    @GET(Endpoints.Getusuarios)
    suspend fun getUsuarios(
        @Header("Authorization") token: String?
    ): Response<List<Usuario>>

    @HTTP(method = "DELETE", path = Endpoints.EliminarUsuarios, hasBody = true)
    suspend fun eliminarUsuarios(
        @Header("Authorization") token : String,
        @Body solicitud: EliminarUsuariosSolicitud
    ): Response<EliminarUsuariosRespuesta>
}