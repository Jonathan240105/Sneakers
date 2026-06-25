package com.example.snkrsapp.Domain

data class ModelIncidenciasAdmin(
    val cargando: Boolean = false,
    val incidencias: List<Incidencia> = emptyList(),
    val idRespondiendo: Int? = null,
    val error: String? = null,
    val mensaje: String? = null
)
