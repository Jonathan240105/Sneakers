package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.Repository.ProductoRepository.ProductoRepository
import com.example.snkrsapp.Domain.ModelPrincipal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrincipalViewModel @Inject constructor(
    private val productoRepository: ProductoRepository
) : ViewModel() {

    private val _model = MutableStateFlow(ModelPrincipal())
    val model = _model.asStateFlow()
    private var salto = 0
    private val limite = 20

    init {
        cargarPaginaProductos()
    }

    fun cargarPaginaProductos() {
        if (_model.value.cargando) return

        _model.update { it.copy(cargando = true) }

        viewModelScope.launch {
            try {
                productoRepository.traerPaginaProductos(limite, salto).collect { nuevosProductos ->
                    if (nuevosProductos.isNotEmpty()) {
                        _model.update {
                            val listaActualizada = (it.listaDeproductos + nuevosProductos)

                            it.copy(
                                listaDeproductos = listaActualizada,
                                exito = true,
                                cargando = false
                            )
                        }
                    } else {
                        _model.update { it.copy(cargando = false) }
                    }
                }
                salto += limite
            } catch (e: Exception) {
                _model.update { it.copy(cargando = false, exito = false) }
                println(e.message)
            }
        }
    }
}
