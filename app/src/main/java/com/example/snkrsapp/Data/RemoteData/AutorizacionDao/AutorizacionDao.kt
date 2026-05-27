package com.example.snkrsapp.Data.RemoteData.AutorizacionDao

import com.example.snkrsapp.Data.RemoteData.PublicacionDao.PublicacionCarritoRespuesta
import com.example.snkrsapp.Data.RemoteData.Variables.Endpoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

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
        @Header("Authorization") token: String?
    ): Response<Usuario>

    @GET("/usuarios/perfil/carrito")
    suspend fun obtenerCarrito(
        @Header("Authorization") token: String
    ): Response<List<PublicacionCarritoRespuesta>>

    @GET("/usuarios/{uid}/coleccion")
    suspend fun obtenerColeccion(
        @Header("Authorization") token: String,
        @Path("uid") uid: String
    ): Response<List<PublicacionCarritoRespuesta>>

    @GET("/usuarios/{uid}/ventas")
    suspend fun obtenerVentas(
        @Header("Authorization") token: String,
        @Path("uid") uid: String
    ): Response<List<PublicacionCarritoRespuesta>>
}