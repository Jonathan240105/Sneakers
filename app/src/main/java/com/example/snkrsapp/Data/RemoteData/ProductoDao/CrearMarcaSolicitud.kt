package com.example.snkrsapp.Data.RemoteData.ProductoDao

data class CrearMarcaSolicitud(
    val nombre: String,
    val paisOrigen: String?,
    val fechaFundacion: String?,
    val logoUrl: String,
    val web: String?
)
data class CrearMarcaRespuesta(
    val ok: Boolean,
    val mensaje: String
)
