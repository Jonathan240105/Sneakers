package com.example.snkrsapp.Data.RemoteData.PublicacionDao

data class PublicacionPropiaListado(
    val idCarrito: Int?,
    val idPublicacion: Int,
    val idVariante: Int?,
    val idProducto: Int,
    val idMarca: Int,
    val modelo: String,
    val marca: String,
    val precio: Double,
    val talla: Double,
    val urlFoto: String,
    val estado: String?,
    val idColor: Int?,
    val color: String?,
    val nombreColor: String?,
    val cantidad: Int?,
    val estadoCarrito: String?,
    val estadoPedido: String?
)

data class PublicacionCarritoRespuesta(
    val idProducto: Int,
    val idMarca: Int,
    val modelo: String,
    val marca: String,
    val urlFoto: String,
    val precio: Double
)
