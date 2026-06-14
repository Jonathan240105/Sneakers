package com.example.snkrsapp.Domain

sealed class EstadoCompra {

    object Cargando : EstadoCompra()

    data class Exito(
        val mensaje: String
    ) : EstadoCompra()

    data class Error(
        val mensajeError: String
    ) : EstadoCompra()
}