package com.example.snkrsapp.Domain

sealed class EstadoEliminarUsuarios {
    object Cargando : EstadoEliminarUsuarios()
    data class Exito(val mensaje: String) : EstadoEliminarUsuarios()
    data class Error(val mensajeError: String) : EstadoEliminarUsuarios()

}
