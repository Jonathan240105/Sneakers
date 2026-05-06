package com.example.snkrsapp.Domain

data class Evento(
    val idEvento: Int = 0,
    val titulo: String = "",
    val descripcion: String = "",
    val fechaEvento: String = "",
    val idOrganizador: Int = 0
)