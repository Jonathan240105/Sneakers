package com.example.snkrsapp.Data.RemoteData.ProductoDao

data class CompletarMarcaSolicitud(
    val idMarca: Int,
    val paisOrigen: String,
    val fechaFundacion: String,
    val logoUrl: String,
    val web: String
)

data class CompletarMarcaRespuesta(
    val ok: Boolean,
    val mensaje: String
)
