package com.example.snkrsapp.Domain

data class ModelPantallaMarcasAdmin(
    val listaMarcas: List<Marca> = emptyList(),
    val listaMarcasIncompletas: List<Marca> = emptyList(),
    val cargandoMarcas: Boolean = false,

    val cargandoEliminarMarcas: Boolean = false,
    val mensajeEliminarMarcas: String = "",

    val cargandoCompletarMarca: Boolean = false,
    val mensajeCompletarMarca: String = "",

    val cargandoCrearMarcas: Boolean = false,
    val mensajeCrearMarca: String = ""
)
