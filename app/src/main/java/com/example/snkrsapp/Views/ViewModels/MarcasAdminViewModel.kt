package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.RemoteData.ProductoDao.CrearMarcaSolicitud
import com.example.snkrsapp.Data.Repository.ProductoRepository.ProductoRepository
import com.example.snkrsapp.Domain.EstadoCrearMarca
import com.example.snkrsapp.Domain.EstadoEliminarMarcas
import com.example.snkrsapp.Domain.ModelPantallaMarcasAdmin
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarcasAdminViewModel @Inject constructor(
    private val productoRepository: ProductoRepository
) : ViewModel() {

    private val _model = MutableStateFlow(ModelPantallaMarcasAdmin())
    val model = _model.asStateFlow()

    fun cargarMarcas() {
        if (model.value.cargandoMarcas) return
        _model.update { it.copy(cargandoMarcas = true) }

        viewModelScope.launch {
            try {
                productoRepository.traerMarcas().collect { nuevasMarcas ->
                    val listaMarcasIncompletas = nuevasMarcas.filter { it.logoUrl.isNullOrEmpty() }
                    _model.update {
                        it.copy(
                            listaMarcas = nuevasMarcas,
                            listaMarcasIncompletas = listaMarcasIncompletas,
                            cargandoMarcas = false,
                        )
                    }
                }
            } catch (e: Exception) {
                _model.update {
                    it.copy(
                        cargandoMarcas = false,
                        listaMarcas = emptyList(),
                        listaMarcasIncompletas = emptyList()
                    )
                }
            }
        }
    }

    fun eliminarMarcas(marcas: List<Int>) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result.token
                if (token != null) {
                    viewModelScope.launch {
                        _model.update { it.copy(cargandoEliminarMarcas = true) }
                        productoRepository.eliminarMarcas(token, marcas).collect { respuesta ->
                            if (respuesta is EstadoEliminarMarcas.Exito) {
                                _model.update {
                                    it.copy(
                                        mensajeEliminarMarcas = respuesta.mensaje,
                                        cargandoEliminarMarcas = false
                                    )
                                }
                            } else if (respuesta is EstadoEliminarMarcas.Error) {
                                _model.update {
                                    it.copy(
                                        mensajeEliminarMarcas = respuesta.mensajeError,
                                        cargandoEliminarMarcas = false
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun completarMarca(
        idMarca: Int,
        pais: String,
        fecha: String,
        logo: String,
        webUrl: String
    ) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result.token
                if (token != null) {
                    viewModelScope.launch {
                        _model.update { it.copy(cargandoCompletarMarca = true) }
                        productoRepository.completarRegistroMarca(
                            token, idMarca, pais, fecha, logo, webUrl
                        ).collect { respuesta ->
                            if (respuesta.ok) {
                                _model.update {
                                    it.copy(
                                        mensajeCompletarMarca = respuesta.mensaje,
                                        cargandoCompletarMarca = false
                                    )
                                }
                            } else {
                                _model.update {
                                    it.copy(
                                        mensajeCompletarMarca = respuesta.mensaje,
                                        cargandoCompletarMarca = false
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun crearMarca(body: CrearMarcaSolicitud) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result.token
                if (token != null) {
                    viewModelScope.launch {
                        _model.update { it.copy(cargandoCrearMarcas = true) }
                        productoRepository.crearMarca(token, body).collect { respuesta ->
                            if (respuesta is EstadoCrearMarca.Exito) {
                                _model.update {
                                    it.copy(
                                        mensajeCrearMarca = respuesta.mensaje,
                                        cargandoCrearMarcas = false
                                    )
                                }
                            } else if (respuesta is EstadoCrearMarca.Error) {
                                _model.update {
                                    it.copy(
                                        mensajeCrearMarca = respuesta.mensajeError,
                                        cargandoCrearMarcas = false
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        cargarMarcas()
    }
}