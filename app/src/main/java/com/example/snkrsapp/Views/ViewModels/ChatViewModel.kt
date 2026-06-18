package com.example.snkrsapp.Views.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snkrsapp.Data.Repository.ChatRepository.ChatRepository
import com.example.snkrsapp.Domain.ModelChat
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _model = MutableStateFlow(ModelChat())
    val model = _model.asStateFlow()

    fun prepararUsuarioActual() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                chatRepository.prepararUsuarioChat(uid)
            } catch (e: Exception) {
                _model.update { it.copy(error = e.message ?: "Error al preparar el chat") }
            }
        }
    }

    fun prepararUsuarioActual(nombreUsuario: String, fotoUsuario: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                chatRepository.prepararUsuarioChat(
                    uid = uid,
                    nombreUsuario = nombreUsuario,
                    fotoUsuario = fotoUsuario
                )
            } catch (e: Exception) {
                _model.update { it.copy(error = e.message ?: "Error al preparar el chat") }
            }
        }
    }

    fun observarChatsUsuarioActual() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                chatRepository.observarChats(uid).collect { chats ->
                    _model.update {
                        it.copy(
                            chats = chats,
                            error = ""
                        )
                    }
                }
            } catch (e: Exception) {
                _model.update {
                    it.copy(error = e.message ?: "No se pudieron cargar las conversaciones")
                }
            }
        }
    }

    fun crearORecuperarChat(
        uidOtroUsuario: String,
        idProducto: Int?,
        nombreUsuarioActual: String,
        fotoUsuarioActual: String,
        nombreOtroUsuario: String,
        modeloProducto: String,
        chatCreado: (String) -> Unit,
        errorChat: (String) -> Unit = {}
    ) {
        val uidActual = FirebaseAuth.getInstance().currentUser?.uid
        if (uidActual == null) {
            errorChat("No hay usuario autenticado")
            return
        }

        viewModelScope.launch {
            _model.update { it.copy(cargando = true, error = "") }

            try {
                val idChat = chatRepository.crearORecuperarChat(
                    uidActual = uidActual,
                    uidOtroUsuario = uidOtroUsuario,
                    idProducto = idProducto,
                    nombreUsuarioActual = nombreUsuarioActual,
                    fotoUsuarioActual = fotoUsuarioActual,
                    nombreOtroUsuario = nombreOtroUsuario,
                    modeloProducto = modeloProducto
                )

                _model.update {
                    it.copy(
                        idChat = idChat,
                        cargando = false,
                        error = ""
                    )
                }
                chatCreado(idChat)
            } catch (e: Exception) {
                val mensajeError = e.message ?: "No se pudo crear el chat"
                _model.update {
                    it.copy(
                        cargando = false,
                        error = mensajeError
                    )
                }
                errorChat(mensajeError)
            }
        }
    }

    fun observarMensajes(idChat: String) {
        val uidActual = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch {
            _model.update { it.copy(idChat = idChat, cargando = true, error = "") }

            try {
                val chat = chatRepository.obtenerChat(idChat)
                _model.update { it.copy(chatActual = chat) }

                chatRepository.observarMensajes(idChat, uidActual).collect { mensajes ->
                    _model.update {
                        it.copy(
                            mensajes = mensajes,
                            cargando = false,
                            error = ""
                        )
                    }
                }
            } catch (e: Exception) {
                _model.update {
                    it.copy(
                        cargando = false,
                        error = e.message ?: "No se pudieron cargar los mensajes"
                    )
                }
            }
        }
    }

    fun cambiarTextoMensaje(texto: String) {
        _model.update { it.copy(textoMensaje = texto) }
    }

    fun enviarMensaje() {
        val uidActual = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val estado = _model.value

        viewModelScope.launch {
            try {
                chatRepository.enviarMensaje(
                    idChat = estado.idChat,
                    uidEmisor = uidActual,
                    texto = estado.textoMensaje
                )
                _model.update { it.copy(textoMensaje = "", error = "") }
            } catch (e: Exception) {
                _model.update { it.copy(error = e.message ?: "No se pudo enviar el mensaje") }
            }
        }
    }
}
