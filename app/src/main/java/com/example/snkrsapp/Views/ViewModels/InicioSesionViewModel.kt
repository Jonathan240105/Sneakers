package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario
import com.example.snkrsapp.Data.Repository.UsuarioRepository.UsuarioRepository
import com.example.snkrsapp.Domain.EstadoLogin
import com.example.snkrsapp.Domain.ModelInicioSesion
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InicioSesionViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _model = MutableStateFlow(ModelInicioSesion())
    val model = _model.asStateFlow()

    fun iniciarSesion(email: String, contra: String) {
        viewModelScope.launch {
            usuarioRepository.iniciarSesion(email, contra).collect { resultado ->

                when (resultado) {
                    is EstadoLogin.Exito -> {
                        _model.update {
                            it.copy(
                                exito = true,
                                cargando = false,
                                usuario = resultado.usuario,
                                error = ""
                            )
                        }
                    }
                    is EstadoLogin.Error -> {
                        _model.update {
                            it.copy(
                                exito = false,
                                cargando = false,
                                error = resultado.mensaje,
                                errorFirebase = resultado.errorFirebase
                            )
                        }
                    }
                    is EstadoLogin.Cargando -> {
                        _model.update { it.copy(cargando = true) }
                    }
                }
            }
        }
    }

    fun cambiarEmail(email: String) {
        _model.update { it.copy(email = email) }
    }

    fun cambiarContra(contra: String) {
        _model.update { it.copy(contra = contra) }
    }

    fun recuperarContrasena(email: String) {
        val emailRecuperacion = email.trim()

        if (emailRecuperacion.isBlank()) {
            _model.update {
                it.copy(
                    cargandoRecuperacion = false,
                    mensajeRecuperacion = "",
                    errorRecuperacion = "Introduce el email asociado a tu cuenta"
                )
            }
            return
        }

        _model.update {
            it.copy(
                cargandoRecuperacion = true,
                mensajeRecuperacion = "",
                errorRecuperacion = ""
            )
        }

        FirebaseAuth.getInstance()
            .sendPasswordResetEmail(emailRecuperacion)
            .addOnCompleteListener { tarea ->
                if (tarea.isSuccessful) {
                    _model.update {
                        it.copy(
                            cargandoRecuperacion = false,
                            mensajeRecuperacion = "Revisa tu correo para restablecer la contraseña",
                            errorRecuperacion = ""
                        )
                    }
                } else {
                    _model.update {
                        it.copy(
                            cargandoRecuperacion = false,
                            mensajeRecuperacion = "",
                            errorRecuperacion = tarea.exception?.message
                                ?: "No se pudo enviar el correo de recuperación"
                        )
                    }
                }
            }
    }

    fun limpiarEstadoRecuperacion() {
        _model.update {
            it.copy(
                cargandoRecuperacion = false,
                mensajeRecuperacion = "",
                errorRecuperacion = ""
            )
        }
    }

    fun resetearEstadoPantalla() {
        _model.update {
            it.copy(
                exito = false,
                errorFirebase = false,
                error = "",
                cargando = false,
                email = "",
                contra = "",
                usuario = Usuario(),
                cargandoRecuperacion = false,
                mensajeRecuperacion = "",
                errorRecuperacion = ""
            )
        }
    }
}
