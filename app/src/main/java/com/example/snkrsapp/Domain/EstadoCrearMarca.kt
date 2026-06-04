package com.example.snkrsapp.Domain

sealed class EstadoCrearMarca {
    object Cargando : EstadoCrearMarca()
    data class Exito(val mensaje: String) : EstadoCrearMarca()
    data class Error(val mensajeError: String) : EstadoCrearMarca()
}