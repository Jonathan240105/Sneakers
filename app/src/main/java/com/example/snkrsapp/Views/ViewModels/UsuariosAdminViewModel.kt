package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.Repository.UsuarioRepository.UsuarioRepository
import com.example.snkrsapp.Domain.EstadoEliminarUsuarios
import com.example.snkrsapp.Domain.ModelPantallaUsuariosAdmin
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuariosAdminViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _model = MutableStateFlow(ModelPantallaUsuariosAdmin())
    val model = _model.asStateFlow()

    fun cargarUsuarios() {
        FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener {

            if (it.isSuccessful) {
                val token = it.result.token
                if (token != null) {
                    viewModelScope.launch {
                        usuarioRepository.getUsuarios(token).collect { listaUsuarios ->
                            _model.update { it.copy(listaUsuarios = listaUsuarios) }
                        }
                    }
                }
            }
        }
    }

    fun eliminarUsuarios(uids: List<String>) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result.token
                if (token != null) {
                    viewModelScope.launch {
                        _model.update { it.copy(cargandoEliminarUsuarios = true) }
                        usuarioRepository.eliminarUsuarios(token, uids).collect { respuesta ->
                            if (respuesta is EstadoEliminarUsuarios.Exito) {
                                _model.update {
                                    it.copy(
                                        mensajeEliminarUsuarios = respuesta.mensaje,
                                        cargandoEliminarUsuarios = false
                                    )
                                }

                            } else if (respuesta is EstadoEliminarUsuarios.Error) {
                                _model.update {
                                    it.copy(
                                        mensajeEliminarUsuarios = respuesta.mensajeError,
                                        cargandoEliminarUsuarios = false
                                    )
                                }
                            } else {
                                _model.update {
                                    it.copy(
                                        mensajeEliminarUsuarios = "Error desconocido",
                                        cargandoEliminarUsuarios = false
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}