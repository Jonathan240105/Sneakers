package com.example.snkrsapp.Data.RemoteData.AutorizacionDao

data class RegistroRespuesta(
    val exito: Boolean = false
)

data class UsuarioSolicitud(
    val UID: String = "",
    val nombreUsuario: String = "",
    val email: String = "",
    val apellidos: String = "",
    val fechaNacimiento: String = ""
)