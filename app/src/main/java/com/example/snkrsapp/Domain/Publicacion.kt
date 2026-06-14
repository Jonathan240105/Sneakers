package com.example.snkrsapp.Domain

data class Publicacion(
    val idPublicacion: Int = 0,
    val idProducto: Int = 0,
    val uidUsuario: String = "",
    val nombreUsuario: String? = "",
    val precio: Double = 0.0,
    val talla: Double = 0.0,
    val estado: String = "",
    val urlFoto: String = "",
    val fechaPublicacion: String = "",
)