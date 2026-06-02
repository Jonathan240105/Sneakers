package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.Repository.ProductoRepository.ProductoRepository
import com.example.snkrsapp.Domain.ModelProductoDetallado
import com.example.snkrsapp.Domain.Publicacion
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductoDetalladoViewModel @Inject constructor(
    private val productoRepository: ProductoRepository
) : ViewModel() {
    private val _model = MutableStateFlow(ModelProductoDetallado())
    val model = _model.asStateFlow()

    fun cargarProducto(idProducto: Int) {
        viewModelScope.launch {
            productoRepository.traerProductoPorId(idProducto).collect { producto ->
                _model.update {
                    it.copy(productoSeleccionado = producto)
                }
            }
        }
    }

    fun cargarPublicacionesDelProducto(idProducto: Int) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result.token
                if (token != null) {
                    viewModelScope.launch {
                        _model.update { it.copy(cargandoPublicaciones = true) }
                        productoRepository.traerPublicacionesPorProducto(token, idProducto)
                            .collect { lista ->
                                _model.update {
                                    it.copy(
                                        listaPublicaciones = lista,
                                        publicacionSeleccionada = lista.firstOrNull()
                                            ?: Publicacion(),
                                        cargandoPublicaciones = false
                                    )
                                }
                            }
                    }
                }
            }
        }
    }

    fun agregarACarrito() {
        FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result.token
                if (token != null) {
                    viewModelScope.launch {
                        productoRepository.agregarAlCarrito(
                            token,
                            _model.value.publicacionSeleccionada.idPublicacion
                        ).collect {
                            if (it) {
                                _model.update {
                                    it.copy(mensaje = "Agregado al carrito")
                                }
                            } else {
                                _model.update {
                                    it.copy(mensaje = "Error al agregar al carrito")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun limpiarMensaje() {
        viewModelScope.launch {
            _model.update {
                it.copy(mensaje = "")
            }
        }
    }

    fun seleccionarPublicacion(publicacion: Publicacion) {
        _model.update {
            it.copy(publicacionSeleccionada = publicacion)
        }
    }

    fun cargarMarca(idMarca: Int) {
        viewModelScope.launch {
            productoRepository.traerMarcaPorId(idMarca).collect { marca ->
                _model.update {
                    it.copy(marcaSeleccionada = marca)
                }
            }
        }
    }
}