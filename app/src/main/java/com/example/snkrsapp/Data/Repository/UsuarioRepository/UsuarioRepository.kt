package com.example.snkrsapp.Data.Repository.UsuarioRepository

import com.example.snkrsapp.Data.RemoteData.ActualizacionDao.ActualizarPerfilRespuesta
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario
import com.example.snkrsapp.Domain.EstadoCompra
import com.example.snkrsapp.Domain.EstadoEliminarUsuarios
import com.example.snkrsapp.Domain.EstadoLogin
import com.example.snkrsapp.Domain.EstadoRegistro
import com.example.snkrsapp.Domain.Incidencia
import com.example.snkrsapp.Domain.PedidoRecibido
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
        fecha: String,
        url : String
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

    suspend fun pedirCarrito(
        token: String,
        publicacion: PublicacionPerfilItem
    ): Flow<EstadoCompra>

    suspend fun traerPedidosPendientesVendedor(token: String): Flow<List<PedidoRecibido>>
    suspend fun traerPedidosComprador(token: String): Flow<List<PedidoRecibido>>
    suspend fun responderPedidoVendedor(
        token: String,
        pedido: PedidoRecibido,
        aceptar: Boolean
    ): Flow<EstadoCompra>
    suspend fun confirmarPedidoRecibido(token: String, pedido: PedidoRecibido): Flow<EstadoCompra>
    suspend fun reportarPedido(
        token: String,
        pedido: PedidoRecibido,
        tipo: String,
        descripcion: String?,
        urlImagen: String? = null
    ): Flow<EstadoCompra>

    suspend fun traerIncidenciasAdmin(token: String): Flow<List<Incidencia>>
    suspend fun traerIncidenciasUsuario(token: String): Flow<List<Incidencia>>
    suspend fun responderIncidenciaAdmin(
        token: String,
        incidencia: Incidencia,
        aceptar: Boolean
    ): Flow<EstadoCompra>

    suspend fun procesarPagoCarrito(
        token: String,
        publicacion: PublicacionPerfilItem
    ): Flow<EstadoCompra>
    suspend fun getUsuarios(token: String): Flow<List<Usuario>>

    suspend fun eliminarUsuarios(token: String, uids: List<String>): Flow<EstadoEliminarUsuarios>
}
