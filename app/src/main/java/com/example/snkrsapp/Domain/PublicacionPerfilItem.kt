package com.example.snkrsapp.Domain

data class PublicacionPerfilItem(
    val idPublicacion: Int,
    val idProducto: Int,
    val idMarca: Int,
    val modelo: String,
    val marca: String,
    val precio: Double,
    val talla: Double,
    val urlFoto: String,
    val estado: String?
)

data class ProductoColeccionItem(
    val idProducto: Int,
    val idMarca: Int,
    val modelo: String,
    val marca: String,
    val urlFoto: String,
    val precio: Double
)