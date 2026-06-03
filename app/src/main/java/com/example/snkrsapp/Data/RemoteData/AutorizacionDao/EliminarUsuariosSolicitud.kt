package com.example.snkrsapp.Data.RemoteData.AutorizacionDao

data class EliminarUsuariosSolicitud(
    val uids: List<String>
)

data class EliminarUsuariosRespuesta(
    val ok: Boolean,
    val mensaje: String
)