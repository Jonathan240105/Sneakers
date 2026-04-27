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

    init {
        listarProductos()
    }
    fun listarProductos() {
        _model.update { it.copy(cargando = true, exito = false) }
        viewModelScope.launch {
            try {
                productoRepository.obtenerProductos().collect {
                    _model.value =
                        _model.value.copy(listaDeproductos = it, exito = true, cargando = false)
                }
            } catch (e: Exception) {
                _model.update {
                    it.copy(cargando = false, exito = false)
                }
            }
        }
    }
}
