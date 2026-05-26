package com.example.snkrsapp.Domain

data class Publicacion(
    val idPublicacion: Int,
    val idProducto: Int,
    val uidUsuario: String,
    val precio: Double,
    val talla: Double,
    val estado: String,
    val urlFoto: String,
    val fechaPublicacion: String,
    val disponible: Boolean
)