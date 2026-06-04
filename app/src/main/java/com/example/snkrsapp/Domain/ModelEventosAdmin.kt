package com.example.snkrsapp.Domain

data class ModelEventosAdmin(
    val cargandoEventos : Boolean = false,
    val listaEventos : List<Evento> = emptyList(),

    val cargandoCrearEvento : Boolean = false,
    val mensajeCrearEvento : String = "",

    val cargandoEliminarEvento : Boolean = false,
    val mensajeEliminarEvento : String = ""
)