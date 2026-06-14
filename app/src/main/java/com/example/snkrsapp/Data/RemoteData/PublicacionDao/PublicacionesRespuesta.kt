package com.example.snkrsapp.Data.RemoteData.PublicacionDao

data class PublicacionesRespuesta(
    val idPublicacion: Int,
    val idProducto: Int,
    val uidUsuario: String,
    val precio: Double,
    val talla: Double,
    val estado: String,
    val urlFoto: String,
    val fechaPublicacion: String,
)