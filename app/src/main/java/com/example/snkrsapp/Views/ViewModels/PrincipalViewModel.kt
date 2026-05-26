package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.Repository.ProductoRepository.ProductoRepository
import com.example.snkrsapp.Domain.ModelPrincipal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        cargarMarcas()
    }

    fun cargarPaginaProductos() {
        if (_model.value.cargandoProductos) return

        println("Cargando productos")
        _model.update { it.copy(cargandoProductos = true) }

        viewModelScope.launch {
            try {
                productoRepository.traerPaginaProductos(limite, salto).collect { nuevosProductos ->
                    if (nuevosProductos.isNotEmpty()) {
                        println("Se han cargado ${nuevosProductos.size} productos")
                        _model.update {
                            val listaActualizada = (it.listaDeproductos + nuevosProductos)

                            it.copy(
                                listaDeproductos = listaActualizada,
                                exitoProductos = true,
                                cargandoProductos = false
                            )
                        }
                    } else {
                        _model.update { it.copy(cargandoProductos = false) }
                    }
                }
                salto += limite
            } catch (e: Exception) {
                _model.update { it.copy(cargandoProductos = false, exitoProductos = false) }
                println(e.message)
            }
        }
    }

    fun cargarMarcas() {
        if (model.value.cargandoMarcas) return
        _model.update { it.copy(cargandoMarcas = true) }

        viewModelScope.launch {
            try {
                productoRepository.traerMarcas().collect { nuevasMarcas ->
                    _model.update {
                        it.copy(
                            listaMarcas = it.listaMarcas + nuevasMarcas,
                            exitoMarcas = true,
                            cargandoMarcas = false
                        )
                    }
                }
            } catch (e: Exception) {
                _model.update { it.copy(cargandoMarcas = false, exitoMarcas = false) }
                println(e.message)
            }
        }
    }
}
