package com.example.snkrsapp.Domain

data class Mensaje(
    val idMensaje: String = "",
    val idChat: String = "",
    val uidEmisor: String = "",
    val texto: String = "",
    val fecha: Long = 0L,
    val leido: Boolean = false
)
