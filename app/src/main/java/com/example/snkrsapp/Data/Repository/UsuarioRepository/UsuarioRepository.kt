package com.example.snkrsapp.Data.Repository.UsuarioRepository

import com.example.snkrsapp.Data.RemoteData.ActualizacionDao.ActualizarPerfilRespuesta
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario
import com.example.snkrsapp.Domain.EstadoCompra
import com.example.snkrsapp.Domain.EstadoEliminarUsuarios
import com.example.snkrsapp.Domain.EstadoLogin
import com.example.snkrsapp.Domain.EstadoRegistro
import com.example.snkrsapp.Domain.ProductoColeccionItem
import com.example.snkrsapp.Domain.PublicacionPerfilItem
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

    suspend fun traerPerfil(token: String, uidSoli: String?, miUid: String): Flow<Usuario>
    suspend fun actualizarPerfil(
        token: String,
        nombre: String?,
        email: String?,
        apellidos: String?,
        contra: String?,
        uid: String
    ): Flow<Usuario?>

    suspend fun traerCarrito(token: String, uidUsuario: String): Flow<List<PublicacionPerfilItem>>
    suspend fun traerColeccionUsuario(
        token: String,
        uidObjetivo: String
    ): Flow<List<ProductoColeccionItem>>

    suspend fun traerVentasUsuario(
        token: String,
        uidObjetivo: String
    ): Flow<List<PublicacionPerfilItem>>

    suspend fun procesarPagoCarrito(token: String): Flow<EstadoCompra>
    suspend fun getUsuarios(token: String): Flow<List<Usuario>>

    suspend fun eliminarUsuarios(token: String, uids: List<String>): Flow<EstadoEliminarUsuarios>
}