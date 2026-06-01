package com.example.snkrsapp.Domain

data class ModelRegistro(
    val exito: Boolean = false,
    val cargando: Boolean = false,
    val errorFirebase: Boolean = false,
    val error : String? = "",
    val nombreUsuario: String = "",
    val apellidos: String = "",
    val email: String = "",
    val contra: String = "",
    val fecha: String = ""

)
