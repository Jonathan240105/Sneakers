package com.example.snkrsapp.Domain

sealed class EstadoEliminarMarcas {
    object Cargando : EstadoEliminarMarcas()
    data class Exito(val mensaje: String) : EstadoEliminarMarcas()
    data class Error(val mensajeError: String) : EstadoEliminarMarcas()
}