package com.example.snkrsapp.Domain

data class PedidoRecibido(
    val idPedido: Int,
    val idPublicacion: Int,
    val idVariante: Int,
    val uidComprador: String,
    val nombreComprador: String,
    val nombreVendedor: String = "",
    val modelo: String,
    val marca: String,
    val urlFoto: String,
    val color: String,
    val talla: Double = 0.0,
    val cantidad: Int,
    val precioUnidad: Double,
    val estado: String,
    val fechaPedido: String? = null,
    val fechaEnvio: String? = null,
    val fechaLimiteConfirmacion: String? = null
)
