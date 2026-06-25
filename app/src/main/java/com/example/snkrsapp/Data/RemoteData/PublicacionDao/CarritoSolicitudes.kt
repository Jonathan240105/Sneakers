package com.example.snkrsapp.Data.RemoteData.PublicacionDao

data class AgregarCarritoSolicitud(
    val idVariante: Int,
    val cantidad: Int
)

data class PedirCarritoSolicitud(
    val idPublicacion: Int,
    val idColor: Int?,
    val cantidad: Int
)

data class ComprarCarritoSolicitud(
    val idPublicacion: Int
)

data class ColorPublicacionRespuesta(
    val idColor: Int,
    val nombre: String,
    val hex: String?
)

data class OperacionCarritoRespuesta(
    val ok: Boolean,
    val message: String? = null
)
