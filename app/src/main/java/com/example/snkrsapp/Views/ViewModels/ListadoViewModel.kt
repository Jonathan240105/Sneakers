package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.Repository.UsuarioRepository.UsuarioRepository
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

    val miPropioUid: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    fun cargarDatosPerfil() {
        val usuarioFirebase = FirebaseAuth.getInstance().currentUser
        val uid = usuarioFirebase?.uid ?: return

        usuarioFirebase.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token
                if (token != null) {
                    _model.update { it.copy(cargandoColeccion = true) }
                    viewModelScope.launch {
                        productoRepository.traerColeccionUsuario(token, uid).collect { lista ->
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
                        productoRepository.traerVentasUsuario(token, uid).collect { lista ->
                            _model.update {
                                it.copy(
                                    listaVentas = lista,
                                    cargandoVentas = false,
                                    exitoVentas = true
                                )
                            }
                        }
                    }

                    _model.update { it.copy(cargandoCarrito = true) }
                    viewModelScope.launch {
                        productoRepository.traerCarrito(token, uid).collect { lista ->
                            _model.update {
                                it.copy(
                                    listaCarrito = lista,
                                    cargandoCarrito = false,
                                    exitoCarrito = true
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun procesarCompra() {
        viewModelScope.launch {
            val productosAComprar = _model.value.listaCarrito
            if (productosAComprar.isNotEmpty()) {
                println("Procesando la compra de ${productosAComprar.size} zapatillas.")
            }
        }
    }
}