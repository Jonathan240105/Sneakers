package com.example.snkrsapp.Data.RemoteData.AutorizacionDao

data class InicioSesionRespuesta(
    val message: String = "",
    val usuario: Usuario? = Usuario()
)

data class Usuario(
    val UID: String = "",
    val nombreUsuario: String = "",
    val email: String = "",
    val apellidos: String = "",
    val fechaNacimiento: String = "",
    val saldo: Double = 0.0,
    val urlFoto: String = ""
)