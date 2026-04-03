package com.example.snkrsapp.Data.RemoteData.InicioSesion

data class InicioSesionRespuesta(
    val message: String = "",
    val usuario: Usuario? = Usuario()
)

data class Usuario(
    val UID: String = "",
    val nombreUsuario: String = "",
    val email: String = "",
    val apellidos: String = "",
    val fechaNacimiento: String = ""
)