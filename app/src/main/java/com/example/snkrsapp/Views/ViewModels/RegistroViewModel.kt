package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.Repository.UsuarioRepository.UsuarioRepository
import com.example.snkrsapp.Domain.EstadoRegistro
import com.example.snkrsapp.Domain.ModelRegistro
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RegistroViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _model = MutableStateFlow(ModelRegistro())
    val model = _model.asStateFlow()

    //Metodo para validar que la fecha sea
    // correcta y evitar que se cree un usuario en
    // firebase y no en la base de datos
    fun fechaValida(fecha: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.isLenient = false
            sdf.parse(fecha)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun registrarUsuario(
        email: String,
        contra: String,
        nombre: String,
        apellidos: String?,
        fecha: String,
        urlFoto: String
    ) {
        if (!fechaValida(fecha)) {
            println("Formato de fecha incorrecto : año mes y dia bro")
            return
        }

        _model.update {
            it.copy(
                cargando = true,
                intentadoRegistrar = true,
                error = null,
                errorFirebase = false
            )
        }
        viewModelScope.launch {
            usuarioRepository.registrarUsuario(email, contra, nombre, apellidos, fecha, urlFoto)
                .collect { resultado ->
                    when (resultado) {
                        is EstadoRegistro.Exito -> {
                            _model.update { it.copy(exito = true, cargando = false, error = null) }
                        }
                        is EstadoRegistro.Error -> {
                            _model.update {
                                it.copy(
                                    exito = false,
                                    cargando = false,
                                    error = resultado.mensaje,
                                    errorFirebase = resultado.errorFirebase
                                )
                            }
                        }
                        is EstadoRegistro.Cargando -> {
                            _model.update { it.copy(cargando = true) }
                        }
                    }
                }
        }
    }

    fun cambiarNombreUsuario(nombre: String) {
        viewModelScope.launch {
            _model.update {
                it.copy(nombreUsuario = nombre)
            }
        }
    }

    fun cambiarApellidos(apellidos: String) {
        viewModelScope.launch {
            _model.update {
                it.copy(apellidos = apellidos)
            }
        }
    }

    fun cambiarEmail(email: String) {
        viewModelScope.launch {
            _model.update {
                it.copy(email = email)
            }
        }
    }

    fun cambiarContra(contra: String) {
        viewModelScope.launch {
            _model.update {
                it.copy(contra = contra)
            }
        }
    }

    fun cambiarFecha(fecha: String) {
        viewModelScope.launch {
            _model.update {
                it.copy(fecha = fecha)
            }
        }
    }

    fun resetearPantalla() {
        _model.update {
            it.copy(
                exito = false, errorFirebase = false, cargando = false, nombreUsuario = "",
                apellidos = "", email = "", contra = "", fecha = "", intentadoRegistrar = false
            )
        }
    }
}