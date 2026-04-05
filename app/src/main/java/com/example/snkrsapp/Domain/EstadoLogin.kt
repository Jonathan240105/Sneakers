package com.example.snkrsapp.Domain

import com.example.snkrsapp.Data.RemoteData.InicioSesion.Usuario

sealed class EstadoLogin {
    object Cargando : EstadoLogin()
    data class Exito(val usario: Usuario) : EstadoLogin()
    data class Error(val mensaje: String, val errorFirebase: Boolean) : EstadoLogin()
}
