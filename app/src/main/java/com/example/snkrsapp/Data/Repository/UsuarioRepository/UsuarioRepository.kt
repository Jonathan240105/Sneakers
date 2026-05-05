package com.example.snkrsapp.Data.Repository.UsuarioRepository

import com.example.snkrsapp.Data.RemoteData.ActualizacionDao.ActualizarPerfilRespuesta
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario
import com.example.snkrsapp.Domain.EstadoLogin
import com.example.snkrsapp.Domain.EstadoRegistro
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface UsuarioRepository {
    suspend fun iniciarSesion(email: String, contra: String): Flow<EstadoLogin>
    suspend fun registrarUsuario(
        email: String,
        contra: String,
        nombre: String,
        apellidos: String?,
        fecha: String
    ): Flow<EstadoRegistro>

    suspend fun traerPerfil(token: String): Flow<Usuario>
    suspend fun actualizarPerfil(
        token: String,
        nombre: String?,
        email : String?,
        apellidos: String?,
        contra: String?,
        uid: String
    ): Flow<Usuario?>
}