package com.example.snkrsapp.Domain

import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario

data class ModelPantallaUsuariosAdmin(
    val listaUsuarios: List<Usuario> = emptyList(),
    val cargandoEliminarUsuarios: Boolean = false,
    val mensajeEliminarUsuarios: String = ""
)