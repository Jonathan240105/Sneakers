package com.example.snkrsapp.Domain

import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario

data class ModelActualizarPerfil(
    val usuario: Usuario = Usuario(),
    val exito: Boolean = true,
    val cargando: Boolean = false
)
