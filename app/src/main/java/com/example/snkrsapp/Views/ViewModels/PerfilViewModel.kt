package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.Repository.UsuarioRepository.UsuarioRepository
import com.example.snkrsapp.Domain.ModelPerfil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val perfilRepository: UsuarioRepository
) : ViewModel() {

    private val _model = MutableStateFlow(ModelPerfil())
    val model = _model.asStateFlow()

    fun cargarPerfil(token: String) {
        if (_model.value.cargando) return

        _model.update { it.copy(cargando = true) }
        viewModelScope.launch {
            perfilRepository.traerPerfil(token).collect { usuario ->
                _model.update { it.copy(usuarioActual = usuario, exito = true, cargando = false) }
            }
        }
    }
}