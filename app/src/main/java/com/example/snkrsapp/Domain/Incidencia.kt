package com.example.snkrsapp.Domain

data class Incidencia(
    val idIncidencia: Int,
    val idPublicacion: Int,
    val idVariante: Int,
    val uidComprador: String,
    val nombreComprador: String,
    val nombreVendedor: String,
    val modelo: String,
    val color: String,
    val talla: String,
    val cantidad: String,
    val tipo: String,
    val descripcion: String,
    val urlImagen: String,
    val estado: String,
    val fechaCreacion: String
)
