package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.RemoteData.EventoDao.CrearEventoSolicitud
import com.example.snkrsapp.Data.Repository.EventoRepository.EventoRepository
import com.example.snkrsapp.Domain.EstadoEliminarEventos
import com.example.snkrsapp.Domain.ModelEventosAdmin
import com.example.snkrsapp.Domain.ModelPantallaEventos
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PantallaEventosViewModel @Inject constructor(
    private val eventoRepository: EventoRepository
) : ViewModel() {

    private val _model = MutableStateFlow(ModelEventosAdmin())
    val model = _model.asStateFlow()


    fun cargarEventos() {
        FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result.token
                if (token != null) {
                    viewModelScope.launch {
                        _model.update { it.copy(cargandoEventos = true) }
                        eventoRepository.getEventosAdmin(token).collect { eventos ->
                            _model.update {
                                it.copy(
                                    listaEventos = eventos, cargandoEventos = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun eliminarEventos(ids: List<Int>) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result.token
                if (token != null) {
                    viewModelScope.launch {
                        eventoRepository.eliminarEventos(token, ids).collect { estado ->
                            if (estado is EstadoEliminarEventos.Exito) {
                                cargarEventos()
                            } else if (estado is EstadoEliminarEventos.Error) {
                                _model.update { it.copy(mensajeEliminarEvento = estado.mensaje) }
                            }
                        }
                    }
                }
            }
        }
    }

    fun crearEvento(titulo: String, descripcion: String, fecha: String) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result.token
                if (token != null) {
                    viewModelScope.launch {
                        eventoRepository.crearEvento(
                            token, CrearEventoSolicitud(titulo, descripcion, fecha)
                        ).collect { respuesta ->
                            if (respuesta.exito) {
                                cargarEventos()
                                _model.update { it.copy(mensajeCrearEvento = respuesta.mensaje) }
                            } else {
                                _model.update { it.copy(mensajeCrearEvento = respuesta.mensaje) }
                            }

                        }
                    }
                }
            }
        }
    }
}