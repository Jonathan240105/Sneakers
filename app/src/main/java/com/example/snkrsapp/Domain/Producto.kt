package com.example.snkrsapp.Domain

data class Producto(
    val idProducto: Int? = null,
    val idMarca: Int = 0,
    val modelo: String = "",
    val precio: Double = 0.0,
    val imagenUrl: String? = ""
)
