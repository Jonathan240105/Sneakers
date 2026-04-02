package com.example.snkrsapp.Data.RemoteData

import com.example.snkrsapp.Domain.Usuario

data class InicioSesionRespuesta(
    val message: String = "",
    val usuario: Usuario? = Usuario()
)
