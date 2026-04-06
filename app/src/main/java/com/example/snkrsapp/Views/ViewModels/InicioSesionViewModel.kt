package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.Repository.Repository
import com.example.snkrsapp.Domain.EstadoLogin
import com.example.snkrsapp.Domain.ModelInicioSesion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InicioSesionViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _model = MutableStateFlow(ModelInicioSesion())
    val model = _model.asStateFlow()

    fun iniciarSesion(email: String, contra: String) {
        viewModelScope.launch {
            repository.iniciarSesion(email, contra).collect { resultado ->

                if (resultado is EstadoLogin.Exito) {
                    _model.update { it.copy(exito = true, cargando = false) }
                    println("Todo fue bien")
                } else if (resultado is EstadoLogin.Error && resultado.errorFirebase) {
                    _model.update { it.copy(exito = false, errorFirebase = true, cargando = false) }
                    println("Algo fue mal,culpa de firebase")
                } else if (resultado is EstadoLogin.Error) {
                    _model.update {
                        it.copy(exito = false, errorFirebase = false, cargando = false)
                    }
                    println("Algo fue mal con el servidor")

                }
            }
        }
    }
}