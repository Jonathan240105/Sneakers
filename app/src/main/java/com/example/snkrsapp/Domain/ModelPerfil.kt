package com.example.snkrsapp.Domain

import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario

data class ModelPerfil(
    val usuarioActual: Usuario = Usuario(),
    val exito : Boolean = true,
    val cargando : Boolean = false
)
