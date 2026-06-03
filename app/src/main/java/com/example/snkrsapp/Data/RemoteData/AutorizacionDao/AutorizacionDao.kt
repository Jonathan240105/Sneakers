package com.example.snkrsapp.Data.RemoteData.AutorizacionDao

import com.example.snkrsapp.Data.RemoteData.PublicacionDao.PublicacionCarritoRespuesta
import com.example.snkrsapp.Data.RemoteData.Variables.Endpoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
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

    @GET("/usuarios/perfil")
    suspend fun getPerfil(
        @Header("Authorization") token: String?,
        @Query("uid") uid: String?
    ): Response<Usuario>

    @GET("/usuarios")
    suspend fun getUsuarios(
        @Header("Authorization") token: String?
    ): Response<List<Usuario>>

    @HTTP(method = "DELETE", path = "/usuarios/eliminar-bloque", hasBody = true)
    suspend fun eliminarUsuarios(
        @Header("Authorization") token : String,
        @Body solicitud: EliminarUsuariosSolicitud
    ): Response<EliminarUsuariosRespuesta>
}