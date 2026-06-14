package com.example.snkrsapp.Data.RemoteData.EventoDao


data class CrearEventoSolicitud(
    val titulo: String,
    val descripcion: String,
    val fechaEvento: String
)

data class CrearEventoRespuesta(
    val ok: Boolean,
    val message: String,
    val idAgregado: Int?
)
