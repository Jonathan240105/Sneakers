package com.example.snkrsapp.Domain

sealed class EstadoRegistro {
    object Cargando : EstadoRegistro()
    data class Exito(val mensaje: String) : EstadoRegistro()
    data class Error(val mensaje: String, val errorFirebase: Boolean) : EstadoRegistro()

}