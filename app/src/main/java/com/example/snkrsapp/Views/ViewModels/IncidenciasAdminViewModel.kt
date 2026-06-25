package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.Repository.UsuarioRepository.UsuarioRepository
import com.example.snkrsapp.Domain.EstadoCompra
import com.example.snkrsapp.Domain.Incidencia
import com.example.snkrsapp.Domain.ModelIncidenciasAdmin
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncidenciasAdminViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _model = MutableStateFlow(ModelIncidenciasAdmin())
    val model = _model.asStateFlow()

    fun cargarIncidencias() {
        val usuario = FirebaseAuth.getInstance().currentUser ?: return

        usuario.getIdToken(true).addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token ?: return@addOnCompleteListener
                viewModelScope.launch {
                    _model.update { it.copy(cargando = true, error = null) }
                    usuarioRepository.traerIncidenciasAdmin(token).collect { lista ->
                        _model.update {
                            it.copy(cargando = false, incidencias = lista)
                        }
                    }
                }
            }
        }
    }

    fun responderIncidencia(incidencia: Incidencia, aceptar: Boolean) {
        val usuario = FirebaseAuth.getInstance().currentUser ?: return

        usuario.getIdToken(true).addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token ?: return@addOnCompleteListener
                viewModelScope.launch {
                    usuarioRepository.responderIncidenciaAdmin(token, incidencia, aceptar)
                        .collect { resultado ->
                            when (resultado) {
                                is EstadoCompra.Cargando -> _model.update {
                                    it.copy(idRespondiendo = incidencia.idIncidencia, error = null)
                                }
                                is EstadoCompra.Exito -> {
                                    _model.update {
                                        it.copy(idRespondiendo = null, mensaje = resultado.mensaje, error = null)
                                    }
                                    cargarIncidencias()
                                }
                                is EstadoCompra.Error -> _model.update {
                                    it.copy(idRespondiendo = null, error = resultado.mensajeError)
                                }
                            }
                        }
                }
            }
        }
    }

    fun limpiarMensaje() {
        _model.update { it.copy(mensaje = null) }
    }
}
