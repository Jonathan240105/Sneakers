package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.RemoteData.EventoDao.EventosSolicitud
import com.example.snkrsapp.Data.Repository.EventoRepository.EventoRepository
import com.example.snkrsapp.Domain.ModelPantallaEventos
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class EventosViewModel @Inject constructor(
    private val eventoRepository: EventoRepository
) : ViewModel() {

    private val _model = MutableStateFlow(ModelPantallaEventos())
    val model = _model.asStateFlow()

    fun seleccionarDia(dia: Int) {
        _model.update {

            it.copy(
                diaSeleccionado = dia
            )
        }

        val fechaSeleccionada = LocalDate.of(
            model.value.mesSeleccionado.year,
            model.value.mesSeleccionado.monthValue,
            dia
        )

        cargarEventos(fechaSeleccionada)
    }

    fun cambiarTituloNuevoEvento(titulo: String) {
        viewModelScope.launch {
            _model.update { it.copy(tituloNuevoEvento = titulo) }
        }
    }

    fun cambiarDescripcionNuevoEvento(descripcion: String) {
        viewModelScope.launch {
            _model.update { it.copy(descripcionNuevoEvento = descripcion) }
        }
    }

    fun cambiarFechaNuevoEvento(fecha: String) {
        viewModelScope.launch {
            val fechaObj = LocalDateTime.parse(fecha)

            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")

            val fechaJava = java.time.LocalDateTime.of(
                fechaObj.year, fechaObj.monthNumber, fechaObj.dayOfMonth,
                fechaObj.hour, fechaObj.minute
            )

            val fechaBonita = fechaJava.format(formatter)

            _model.update {
                it.copy(
                    fechaNuevoEvento = fechaBonita,
                    fechaNuevoEventoBd = fecha
                )
            }
        }
    }

    fun resetearModelEvento() {
        viewModelScope.launch {
            _model.update {
                it.copy(
                    tituloNuevoEvento = "",
                    descripcionNuevoEvento = "",
                    fechaNuevoEvento = "",
                    fechaNuevoEventoBd = ""
                )
            }
        }
    }

    fun cargarEventos(fecha: LocalDate) {
        val usuario = FirebaseAuth.getInstance().currentUser

        usuario?.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token
                if (token != null) {
                    _model.update { it.copy(cargandoListaEventos = true) }
                    viewModelScope.launch {
                        eventoRepository.getEventos(usuario.uid, token, fecha.toString())
                            .collect { lista ->
                                _model.update {
                                    it.copy(
                                        listaEventos = lista,
                                        cargandoListaEventos = false,
                                        exitoListaEventos = true
                                    )
                                }
                            }
                    }
                }
            } else {
                _model.update { it.copy(cargandoListaEventos = false, exitoListaEventos = false) }
            }
        }
    }

    fun eliminarEvento(idEvento: Int) {
        val usuario = FirebaseAuth.getInstance().currentUser

        usuario?.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token
                if (token != null) {
                    _model.update { it.copy(cargandoEliminarEvento = true) }
                    viewModelScope.launch {
                        eventoRepository.eliminarEvento(token, idEvento).collect { resultado ->
                            _model.update {
                                it.copy(
                                    exitoEliminarEvento = resultado.exito,
                                    cargandoEliminarEvento = false
                                )
                            }
                            if (resultado.exito) {
                                cargarEventos(LocalDate.now())
                            }
                        }
                    }
                }
            } else {
                _model.update {
                    it.copy(
                        cargandoEliminarEvento = false,
                        exitoEliminarEvento = false
                    )
                }
            }
        }
    }

    fun crearEvento(solicitud: EventosSolicitud) {
        val usuario = FirebaseAuth.getInstance().currentUser

        usuario?.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token

                if (token != null) {
                    _model.update { it.copy(cargandoCrearEvento = true) }
                    viewModelScope.launch {
                        eventoRepository.crearEvento(token, solicitud, usuario.uid)
                            .collect { resultado ->
                                _model.update {
                                    it.copy(
                                        exitoCrearEvento = resultado.exito,
                                        cargandoCrearEvento = false
                                    )
                                }
                                if (resultado.exito) {
                                    cargarEventos(LocalDate.now())
                                }
                            }
                    }
                }
            } else {
                _model.update { it.copy(cargandoCrearEvento = false, exitoCrearEvento = false) }
            }
        }
    }
}