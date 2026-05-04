package com.example.snkrsapp.Data.RemoteData.ActualizacionDao

data class ActualizarPerfilRespuesta(
    val ok: Boolean,
    val message: String
)

data class ActualizarPerfilSolicitud(
    val nombreUsuario: String? = null,
    val apellidos: String? = null,
    val password: String? = null
)
