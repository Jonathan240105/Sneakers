package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario
import com.example.snkrsapp.Data.Repository.UsuarioRepository.UsuarioRepository
import com.example.snkrsapp.Domain.ModelActualizarPerfil
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActualizarPerfilViewModel @Inject constructor(
    private val actualizarPerfilRepository: UsuarioRepository
) : ViewModel() {

    private val _model = MutableStateFlow(ModelActualizarPerfil())
    val model = _model.asStateFlow()

    fun actualizaPerfil(email: String?, nombre: String?, apellidos: String?, password: String?) {

        if (_model.value.cargando) return

        val usuario = FirebaseAuth.getInstance().currentUser
        usuario?.getIdToken(true)?.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result.token
                if (token != null) {
                    val uid = usuario.uid
                    _model.update { it.copy(cargando = true) }
                    viewModelScope.launch {
                        actualizarPerfilRepository.actualizarPerfil(
                            token, nombre, email, apellidos, password, uid
                        ).collect { usuarioNuevo ->
                            _model.update {
                                it.copy(
                                    exito = true,
                                    cargando = false,
                                    usuario = usuarioNuevo ?: Usuario()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun cerrarSesion(){
        FirebaseAuth.getInstance().signOut()
    }
}