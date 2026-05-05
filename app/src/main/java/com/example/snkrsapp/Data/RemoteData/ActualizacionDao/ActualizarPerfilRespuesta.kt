package com.example.snkrsapp.Data.RemoteData.ActualizacionDao

data class ActualizarPerfilRespuesta(
    val ok: Boolean,
    val message: String
)

data class ActualizarPerfilSolicitud(
    val email: String? = null,
    val nombreUsuario: String? = null,
    val apellidos: String? = null
)
