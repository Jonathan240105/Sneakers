package com.example.snkrsapp.Domain

data class ModelChat(
    val idChat: String = "",
    val chatActual: Chat = Chat(),
    val chats: List<Chat> = emptyList(),
    val mensajes: List<Mensaje> = emptyList(),
    val textoMensaje: String = "",
    val cargando: Boolean = false,
    val error: String = ""
)
