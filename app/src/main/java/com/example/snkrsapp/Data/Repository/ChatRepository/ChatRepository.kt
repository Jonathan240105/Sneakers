package com.example.snkrsapp.Data.Repository.ChatRepository

import com.example.snkrsapp.Domain.Chat
import com.example.snkrsapp.Domain.Mensaje
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun prepararUsuarioChat(
        uid: String,
        nombreUsuario: String = "",
        fotoUsuario: String = ""
    )

    suspend fun crearORecuperarChat(
        uidActual: String,
        uidOtroUsuario: String,
        idProducto: Int?,
        nombreUsuarioActual: String,
        fotoUsuarioActual: String,
        nombreOtroUsuario: String,
        modeloProducto: String
    ): String

    fun observarChats(uidUsuario: String): Flow<List<Chat>>

    suspend fun obtenerChat(idChat: String): Chat

    fun observarMensajes(idChat: String, uidActual: String): Flow<List<Mensaje>>

    suspend fun enviarMensaje(idChat: String, uidEmisor: String, texto: String)
}
