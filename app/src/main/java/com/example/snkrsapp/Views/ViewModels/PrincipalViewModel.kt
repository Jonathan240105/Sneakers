package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.Repository.ProductoRepository.ProductoRepository
import com.example.snkrsapp.Domain.ModelPrincipal
import com.google.firebase.auth.FirebaseAuth
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



    fun cambiarRangoPrecio(minPrecio: Int?, maxPrecio: Int?) {
        _model.update {
            it.copy(
                minPrecio = minPrecio,
                maxPrecio = maxPrecio
            )
        }
    }

    fun cambiarTalla(talla: Double?) {
        _model.update { it.copy(talla = talla) }
    }

    fun alternarMarcaTemporal(idMarca: Int) {
        _model.update {
            val listaActualizada = if (it.marcas?.contains(idMarca) == true) {
                it.marcas - idMarca
            } else {
                it.marcas?.plus(idMarca) ?: emptyList()
            }
            it.copy(marcas = listaActualizada)
        }
    }

    fun aplicarFiltrosDesdeHoja() {
        val estado = _model.value
        aplicarFiltros(
            minPrecio = estado.minPrecio?.toDouble(),
            maxPrecio = estado.maxPrecio?.toDouble(),
            talla = estado.talla,
            marcas = estado.marcas?.ifEmpty { null }
        )
    }

    fun aplicarFiltros(
        minPrecio: Double?,
        maxPrecio: Double?,
        talla: Double?,
        marcas: List<Int>?
    ) {
        salto = 0

        _model.update {
            it.copy(
                listaDeproductos = emptyList(),
                cargandoProductos = true,
                esListaFiltrada = true,
                minPrecioSeleccionado = minPrecio,
                maxPrecioSeleccionado = maxPrecio,
                tallaSeleccionado = talla,
                marcasSeleccionadas = marcas
            )
        }

        FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener { tarea ->

            if (tarea.isSuccessful) {
                val token = tarea.result.token
                if (token != null) {

                    viewModelScope.launch {
                        try {
                            productoRepository.traerPaginaProductosFiltrado(
                                token,
                                minPrecio,
                                maxPrecio,
                                talla,
                                marcas,
                                limite,
                                salto
                            ).collect { productosFiltrados ->

                                _model.update { state ->
                                    state.copy(
                                        listaDeproductos = productosFiltrados, // Coloca los filtrados en pantalla
                                        exitoProductos = true,
                                        cargandoProductos = false
                                    )
                                }

                                if (productosFiltrados.isNotEmpty()) {
                                    salto += limite
                                }
                            }
                        } catch (e: Exception) {
                            _model.update {
                                it.copy(
                                    cargandoProductos = false,
                                    exitoProductos = false
                                )
                            }
                            println("Error en ViewModel al filtrar: ${e.message}")
                        }
                    }
                }
            }
        }
    }

    fun getNombreMarca(idMarca : Int) : String{
        val marca = _model.value.listaMarcas.find { it.idMarca == idMarca }
        return marca?.nombre ?: ""
    }
    fun cargarPaginaProductos() {
        if (_model.value.cargandoProductos || _model.value.esBusquedaTexto) return

        _model.update { it.copy(cargandoProductos = true) }
        val estadoActual = _model.value

        FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token
                if (token != null) {

                    viewModelScope.launch {
                        try {
                            val flowProductos = if (estadoActual.esListaFiltrada) {
                                productoRepository.traerPaginaProductosFiltrado(
                                    token,
                                    estadoActual.minPrecioSeleccionado,
                                    estadoActual.maxPrecioSeleccionado,
                                    estadoActual.tallaSeleccionado,
                                    estadoActual.marcasSeleccionadas,
                                    limite,
                                    salto
                                )
                            } else {
                                productoRepository.traerPaginaProductos(limite, salto)
                            }

                            flowProductos.collect { nuevosProductos ->
                                if (nuevosProductos.isNotEmpty()) {
                                    _model.update {
                                        it.copy(
                                            listaDeproductos = it.listaDeproductos + nuevosProductos,
                                            exitoProductos = true,
                                            cargandoProductos = false
                                        )
                                    }
                                    salto += limite
                                } else {
                                    _model.update { it.copy(cargandoProductos = false) }
                                }
                            }
                        } catch (e: Exception) {
                            _model.update {
                                it.copy(
                                    cargandoProductos = false,
                                    exitoProductos = false
                                )
                            }
                            println(e.message)
                        }
                    }
                }
            }

        }
    }

    fun limpiarFiltros() {
        salto = 0
        _model.update {
            it.copy(
                listaDeproductos = emptyList(),
                esListaFiltrada = false,
                minPrecioSeleccionado = null,
                esBusquedaTexto = false,
                maxPrecioSeleccionado = null,
                tallaSeleccionado = null,
                marcasSeleccionadas = null
            )
        }
        cargarPaginaProductos()
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

    fun buscarZapatillasPorTexto(texto: String) {
        if (texto.isBlank()) {
            _model.update { it.copy(textoBusquedaActual = "") }
            limpiarFiltros()
            return
        }

        salto = 0
        _model.update {
            it.copy(
                textoBusquedaActual = texto,
                esBusquedaTexto = true,
                cargandoProductos = true,
                listaDeproductos = emptyList()
            )
        }

        FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token
                if (token != null) {
                    viewModelScope.launch {
                        try {
                            productoRepository.buscarProductosPorTexto(token, texto).collect { resultados ->
                                    _model.update {
                                        it.copy(
                                            listaDeproductos = resultados,
                                            exitoProductos = true,
                                            cargandoProductos = false
                                        )
                                    }
                            }
                        } catch (e: Exception) {
                            _model.update { it.copy(cargandoProductos = false, exitoProductos = false) }
                            println("Error en ViewModel al buscar por texto: ${e.message}")
                        }
                    }
                }
            } else {
                _model.update { it.copy(cargandoProductos = false) }
            }
        }
    }
}
