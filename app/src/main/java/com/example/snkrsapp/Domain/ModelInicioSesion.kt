package com.example.snkrsapp.Domain

import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario

data class ModelInicioSesion(
    val exito : Boolean = false,
    val cargando : Boolean = false,
    val errorFirebase : Boolean = false,
    val error: String = "",
    val email : String = "",
    val contra : String = "",
    val usuario : Usuario = Usuario()
)
