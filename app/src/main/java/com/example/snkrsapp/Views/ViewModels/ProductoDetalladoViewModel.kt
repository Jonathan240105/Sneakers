package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.Repository.ProductoRepository.ProductoRepository
import com.example.snkrsapp.Domain.ModelProductoDetallado
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