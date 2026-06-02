package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.Repository.UsuarioRepository.UsuarioRepository
import com.example.snkrsapp.Domain.ModelPerfil
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val perfilRepository: UsuarioRepository
) : ViewModel() {

    private val _model = MutableStateFlow(ModelPerfil())
    val model = _model.asStateFlow()

    fun cargarPerfil(uidAVisualizar: String? = null) {
        if (_model.value.cargando) return

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val miUidLogueado = firebaseUser?.uid ?: ""

        val esMiPerfil = uidAVisualizar == null || uidAVisualizar == miUidLogueado

        firebaseUser?.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token
                if (token != null) {
                    _model.update {
                        it.copy(
                            cargando = true,
                            esMiPerfil = esMiPerfil
                        )
                    }

                    viewModelScope.launch {
                        perfilRepository.traerPerfil(token, uidAVisualizar, miUidLogueado)
                            .collect { usuario ->
                                _model.update {
                                    it.copy(
                                        usuarioActual = usuario,
                                        exito = true,
                                        cargando = false
                                    )
                                }
                            }
                    }
                }
            } else {
                _model.update {
                    it.copy(
                        exito = false,
                        cargando = false
                    )
                }
            }
        }
    }

    fun cargarListados(uid: String? = null) {
        val usuarioFirebase = FirebaseAuth.getInstance().currentUser
        val miUidLogueado = usuarioFirebase?.uid ?: return

        val esMiPerfil = uid == null || uid == miUidLogueado

        val uidObjetivo = uid ?: miUidLogueado

        usuarioFirebase.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token
                if (token != null) {
                    _model.update { it.copy(cargandoColeccion = true) }
                    viewModelScope.launch {
                        perfilRepository.traerColeccionUsuario(token, uidObjetivo).collect { lista ->
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
                        perfilRepository.traerVentasUsuario(token, uidObjetivo).collect { lista ->
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
                            perfilRepository.traerCarrito(token, miUidLogueado).collect { lista ->
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
}