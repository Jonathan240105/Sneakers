package com.example.snkrsapp.Domain

sealed class EstadoEliminarEventos{
    object Cargando: EstadoEliminarEventos()
    data class Exito(val mensaje: String): EstadoEliminarEventos()
    data class Error(val mensaje: String): EstadoEliminarEventos()
}
