package com.example.snkrsapp.Data.RemoteData.ProductoDao

data class EliminarMarcasSolicitud(
    val idMarcas: List<Int>
)

data class EliminarMarcasRespuesta(
    val ok: Boolean,
    val mensaje: String
)