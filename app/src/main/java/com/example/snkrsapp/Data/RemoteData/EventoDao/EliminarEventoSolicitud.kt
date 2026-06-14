package com.example.snkrsapp.Data.RemoteData.EventoDao

data class EliminarEventoSolicitud(
    val idsEventos: List<Int>
)

data class EliminarEventosRespuesta(
    val ok: Boolean,
    val mensaje: String
)
