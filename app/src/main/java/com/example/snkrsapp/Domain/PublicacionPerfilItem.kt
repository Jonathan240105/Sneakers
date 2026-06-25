package com.example.snkrsapp.Domain

data class PublicacionPerfilItem(
    val idCarrito: Int? = null,
    val idPublicacion: Int,
    val idVariante: Int? = null,
    val idProducto: Int,
    val idMarca: Int,
    val modelo: String,
    val marca: String,
    val precio: Double,
    val talla: Double,
    val urlFoto: String,
    val estado: String?,
    val idColor: Int? = null,
    val nombreColor: String = "",
    val cantidad: Int = 1,
    val estadoPedido: String = "carrito"
)

data class ProductoColeccionItem(
    val idProducto: Int,
    val idMarca: Int,
    val modelo: String,
    val marca: String,
    val urlFoto: String,
    val precio: Double
)
