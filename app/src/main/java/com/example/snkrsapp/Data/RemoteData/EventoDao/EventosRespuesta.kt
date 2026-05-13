package com.example.snkrsapp.Data.RemoteData.EventoDao

data class EventosSolicitud(
    val titulo: String = "",
    val descripcion: String = "",
    val fechaEvento: String = ""
)

data class EventosRespuesta(
    val idEvento: Int,
    val titulo: String,
    val descripcion: String,
    val fechaEvento: String,
    val idOrganizador: String? = null
)

data class EventosRespuestaSimple(
    val ok: Boolean,
    val message: String,
    val idAgregado : Int?
)