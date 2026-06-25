package com.example.snkrsapp.Views.ViewModels

import android.net.Uri
import com.example.snkrsapp.Data.Repository.ProductoRepository.ProductoRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.Repository.UsuarioRepository.UsuarioRepository
import com.example.snkrsapp.Domain.EstadoCompra
import com.example.snkrsapp.Domain.ModelListados
import com.example.snkrsapp.Domain.PedidoRecibido
import com.example.snkrsapp.Domain.PublicacionPerfilItem
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListadoViewModel @Inject constructor(
    private val productoRepository: UsuarioRepository,
    private val productoImagenRepository: ProductoRepository
) : ViewModel() {

    private val _model = MutableStateFlow(ModelListados())
    val model = _model.asStateFlow()

    fun cargarDatosPerfil(uid: String? = null) {

        val usuarioFirebase = FirebaseAuth.getInstance().currentUser
        val miUid = usuarioFirebase?.uid ?: return

        val esMiPerfil = uid == null || uid == miUid
        val uidObjetivo = uid ?: miUid

        usuarioFirebase.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token
                if (token != null) {
                    _model.update { it.copy(cargandoColeccion = true, esMiPerfil = esMiPerfil) }
                    viewModelScope.launch {
                        productoRepository.traerColeccionUsuario(token, uidObjetivo)
                            .collect { lista ->
                                _model.update {
                                    it.copy(
                                        listaColeccion = lista,
                                        cargandoColeccion = false,
                                        exitoColeccion = true
                                    )
                                }
                            }
                    }

                    _model.update { it.copy(cargandoVentas = true) }
                    viewModelScope.launch {
                        productoRepository.traerVentasUsuario(token, uidObjetivo).collect { lista ->
                            _model.update {
                                it.copy(
                                    listaVentas = lista,
                                    cargandoVentas = false,
                                    exitoVentas = true
                                )
                            }
                        }
                    }

                    if (esMiPerfil) {
                        _model.update { it.copy(cargandoCarrito = true) }
                        viewModelScope.launch {
                            productoRepository.traerCarrito(token, miUid).collect { lista ->
                                _model.update {
                                    it.copy(
                                        listaCarrito = lista.filter { articulo ->
                                            articulo.estadoPedido.lowercase() == "carrito"
                                        },
                                        cargandoCarrito = false,
                                        exitoCarrito = true
                                    )
                                }
                            }
                        }

                        _model.update { it.copy(cargandoPedidos = true) }
                        viewModelScope.launch {
                            productoRepository.traerPedidosPendientesVendedor(token).collect { lista ->
                                _model.update {
                                    it.copy(
                                        listaPedidosPendientes = lista,
                                        cargandoPedidos = false
                                    )
                                }
                            }
                        }

                        viewModelScope.launch {
                            productoRepository.traerPedidosComprador(token).collect { lista ->
                                _model.update {
                                    it.copy(listaPedidosComprador = lista)
                                }
                            }
                        }

                        _model.update { it.copy(cargandoIncidencias = true) }
                        viewModelScope.launch {
                            productoRepository.traerIncidenciasUsuario(token).collect { lista ->
                                _model.update {
                                    it.copy(
                                        listaIncidenciasUsuario = lista,
                                        cargandoIncidencias = false
                                    )
                                }
                            }
                        }
                    } else {
                        _model.update {
                            it.copy(
                                listaCarrito = emptyList(),
                                listaPedidosPendientes = emptyList(),
                                listaPedidosComprador = emptyList(),
                                listaIncidenciasUsuario = emptyList(),
                                cargandoCarrito = false,
                                exitoCarrito = false,
                                cargandoPedidos = false,
                                cargandoIncidencias = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun procesarCompra(articulo: PublicacionPerfilItem) {
        val usuarioFirebase = FirebaseAuth.getInstance().currentUser ?: return
        val miUid = usuarioFirebase.uid

        usuarioFirebase.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token
                if (token != null) {
                    viewModelScope.launch {
                        productoRepository.procesarPagoCarrito(token, articulo).collect { resultado ->
                            when (resultado) {
                                is EstadoCompra.Cargando -> {
                                    _model.update {
                                        it.copy(
                                            cargandoPago = true,
                                            idPublicacionPidiendo = articulo.idPublicacion,
                                            error = null
                                        )
                                    }
                                }

                                is EstadoCompra.Exito -> {
                                    _model.update {
                                        it.copy(
                                            cargandoPago = false,
                                            idPublicacionPidiendo = null,
                                            error = null,
                                            mensajeExito = resultado.mensaje
                                        )
                                    }
                                    cargarDatosPerfil(miUid)
                                }

                                is EstadoCompra.Error -> {
                                    _model.update {
                                        it.copy(
                                            cargandoPago = false,
                                            idPublicacionPidiendo = null,
                                            error = resultado.mensajeError
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                _model.update { it.copy(error = "Error de autenticación con el servidor.") }
            }
        }
    }
    fun pedirArticulo(articulo: PublicacionPerfilItem) {
        val usuarioFirebase = FirebaseAuth.getInstance().currentUser ?: return
        val miUid = usuarioFirebase.uid

        usuarioFirebase.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token
                if (token != null) {
                    viewModelScope.launch {
                        productoRepository.pedirCarrito(token, articulo).collect { resultado ->
                            when (resultado) {
                                is EstadoCompra.Cargando -> {
                                    _model.update {
                                        it.copy(
                                            idPublicacionPidiendo = articulo.idPublicacion,
                                            error = null
                                        )
                                    }
                                }

                                is EstadoCompra.Exito -> {
                                    _model.update {
                                        it.copy(
                                            idPublicacionPidiendo = null,
                                            mensajeExito = resultado.mensaje,
                                            error = null
                                        )
                                    }
                                    cargarDatosPerfil(miUid)
                                }

                                is EstadoCompra.Error -> {
                                    _model.update {
                                        it.copy(
                                            idPublicacionPidiendo = null,
                                            error = resultado.mensajeError
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                _model.update { it.copy(error = "Error de autenticación con el servidor.") }
            }
        }
    }

    fun responderPedido(pedido: PedidoRecibido, aceptar: Boolean) {
        responderPedidos(listOf(pedido), aceptar)
    }

    fun responderPedidos(pedidos: List<PedidoRecibido>, aceptar: Boolean) {
        if (pedidos.isEmpty()) return

        val usuarioFirebase = FirebaseAuth.getInstance().currentUser ?: return
        val miUid = usuarioFirebase.uid

        usuarioFirebase.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token
                if (token != null) {
                    viewModelScope.launch {
                        _model.update {
                            it.copy(idPedidoRespondiendo = pedidos.first().idPedido, error = null)
                        }

                        var mensajeError: String? = null
                        var mensajeExito: String? = null

                        pedidos.forEach { pedido ->
                            productoRepository.responderPedidoVendedor(token, pedido, aceptar)
                                .collect { resultado ->
                                    when (resultado) {
                                        is EstadoCompra.Exito -> mensajeExito = resultado.mensaje
                                        is EstadoCompra.Error -> mensajeError = resultado.mensajeError
                                        is EstadoCompra.Cargando -> Unit
                                    }
                                }
                        }

                        _model.update {
                            it.copy(
                                idPedidoRespondiendo = null,
                                mensajeExito = mensajeExito,
                                error = mensajeError
                            )
                        }
                        cargarDatosPerfil(miUid)
                    }
                }
            } else {
                _model.update { it.copy(error = "Error de autenticación con el servidor.") }
            }
        }
    }

    fun confirmarPedidoRecibido(pedido: PedidoRecibido) {
        ejecutarAccionPedidoComprador(pedido) { token ->
            productoRepository.confirmarPedidoRecibido(token, pedido)
        }
    }

    fun confirmarPedidosRecibidos(pedidos: List<PedidoRecibido>) {
        ejecutarAccionPedidosComprador(pedidos) { token, pedido ->
            productoRepository.confirmarPedidoRecibido(token, pedido)
        }
    }

    fun reportarPedido(pedido: PedidoRecibido, tipo: String, descripcion: String?) {
        ejecutarAccionPedidoComprador(pedido) { token ->
            productoRepository.reportarPedido(token, pedido, tipo, descripcion)
        }
    }

    fun reportarPedidos(
        pedidos: List<PedidoRecibido>,
        tipo: String,
        descripcion: String?,
        imagen: Uri? = null
    ) {
        if (pedidos.isEmpty()) return

        val pedidoPrincipal = pedidos.first()
        val usuarioFirebase = FirebaseAuth.getInstance().currentUser ?: return
        val miUid = usuarioFirebase.uid

        usuarioFirebase.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token
                if (token != null) {
                    viewModelScope.launch {
                        _model.update {
                            it.copy(idPedidoRespondiendo = pedidoPrincipal.idPedido, error = null)
                        }

                        val urlImagen = imagen?.let { productoImagenRepository.subirImagenACloudinary(it) }
                        productoRepository.reportarPedido(token, pedidoPrincipal, tipo, descripcion, urlImagen)
                            .collect { resultado ->
                                when (resultado) {
                                    is EstadoCompra.Cargando -> Unit
                                    is EstadoCompra.Exito -> {
                                        _model.update {
                                            it.copy(
                                                idPedidoRespondiendo = null,
                                                mensajeExito = resultado.mensaje,
                                                error = null
                                            )
                                        }
                                        cargarDatosPerfil(miUid)
                                    }

                                    is EstadoCompra.Error -> {
                                        _model.update {
                                            it.copy(
                                                idPedidoRespondiendo = null,
                                                error = resultado.mensajeError
                                            )
                                        }
                                    }
                                }
                            }
                    }
                }
            } else {
                _model.update { it.copy(error = "Error de autenticación con el servidor.") }
            }
        }
    }

    private fun ejecutarAccionPedidoComprador(
        pedido: PedidoRecibido,
        accion: suspend (String) -> kotlinx.coroutines.flow.Flow<EstadoCompra>
    ) {
        val usuarioFirebase = FirebaseAuth.getInstance().currentUser ?: return
        val miUid = usuarioFirebase.uid

        usuarioFirebase.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token
                if (token != null) {
                    viewModelScope.launch {
                        accion(token).collect { resultado ->
                            when (resultado) {
                                is EstadoCompra.Cargando -> {
                                    _model.update {
                                        it.copy(idPedidoRespondiendo = pedido.idPedido, error = null)
                                    }
                                }

                                is EstadoCompra.Exito -> {
                                    _model.update {
                                        it.copy(
                                            idPedidoRespondiendo = null,
                                            mensajeExito = resultado.mensaje,
                                            error = null
                                        )
                                    }
                                    cargarDatosPerfil(miUid)
                                }

                                is EstadoCompra.Error -> {
                                    _model.update {
                                        it.copy(
                                            idPedidoRespondiendo = null,
                                            error = resultado.mensajeError
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                _model.update { it.copy(error = "Error de autenticación con el servidor.") }
            }
        }
    }

    private fun ejecutarAccionPedidosComprador(
        pedidos: List<PedidoRecibido>,
        accion: suspend (String, PedidoRecibido) -> kotlinx.coroutines.flow.Flow<EstadoCompra>
    ) {
        if (pedidos.isEmpty()) return

        val usuarioFirebase = FirebaseAuth.getInstance().currentUser ?: return
        val miUid = usuarioFirebase.uid

        usuarioFirebase.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token
                if (token != null) {
                    viewModelScope.launch {
                        _model.update {
                            it.copy(idPedidoRespondiendo = pedidos.first().idPedido, error = null)
                        }

                        var mensajeError: String? = null
                        var mensajeExito: String? = null

                        pedidos.forEach { pedido ->
                            accion(token, pedido).collect { resultado ->
                                when (resultado) {
                                    is EstadoCompra.Exito -> mensajeExito = resultado.mensaje
                                    is EstadoCompra.Error -> mensajeError = resultado.mensajeError
                                    is EstadoCompra.Cargando -> Unit
                                }
                            }
                        }

                        _model.update {
                            it.copy(
                                idPedidoRespondiendo = null,
                                mensajeExito = mensajeExito,
                                error = mensajeError
                            )
                        }
                        cargarDatosPerfil(miUid)
                    }
                }
            } else {
                _model.update { it.copy(error = "Error de autenticación con el servidor.") }
            }
        }
    }
    fun limpiarMensajeExito() {
        _model.update { it.copy(mensajeExito = null) }
    }
}


