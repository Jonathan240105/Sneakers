package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.Repository.UsuarioRepository.UsuarioRepository
import com.example.snkrsapp.Domain.EstadoCompra
import com.example.snkrsapp.Domain.ModelListados
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListadoViewModel @Inject constructor(
    private val productoRepository: UsuarioRepository
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
                                        listaCarrito = lista,
                                        cargandoCarrito = false,
                                        exitoCarrito = true
                                    )
                                }
                            }
                        }
                    } else {
                        _model.update {
                            it.copy(
                                listaCarrito = emptyList(),
                                cargandoCarrito = false,
                                exitoCarrito = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun procesarCompra() {
        viewModelScope.launch {
            val productosAComprar = _model.value.listaCarrito
            if (productosAComprar.isNotEmpty() && !_model.value.cargandoPago) {

                val usuarioFirebase = FirebaseAuth.getInstance().currentUser
                val miUid = usuarioFirebase?.uid ?: return@launch

                usuarioFirebase.getIdToken(true)?.addOnCompleteListener { tarea ->
                    if (tarea.isSuccessful) {
                        val token = tarea.result.token
                        if (token != null) {

                            viewModelScope.launch {
                                productoRepository.procesarPagoCarrito(token).collect { resultado ->
                                    when (resultado) {
                                        is EstadoCompra.Cargando -> {
                                            _model.update {
                                                it.copy(cargandoPago = true, error = null)
                                            }
                                        }

                                        is EstadoCompra.Exito -> {
                                            _model.update {
                                                it.copy(
                                                    cargandoPago = false,
                                                    listaCarrito = emptyList(),
                                                    error = null
                                                )
                                            }
                                        }

                                        is EstadoCompra.Error -> {
                                            _model.update {
                                                it.copy(
                                                    cargandoPago = false,
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
        }
    }
}