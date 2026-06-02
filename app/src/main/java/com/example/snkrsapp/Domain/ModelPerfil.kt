package com.example.snkrsapp.Domain

import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario

data class ModelPerfil(
    val usuarioActual: Usuario = Usuario(),
    val exito : Boolean = true,
    val cargando : Boolean = false,
    val esMiPerfil : Boolean = true,

    val cargandoColeccion: Boolean = false,
    val exitoColeccion: Boolean = false,

    val cargandoVentas: Boolean = false,
    val exitoVentas: Boolean = false,

    val cargandoCarrito: Boolean = false,
    val exitoCarrito: Boolean = false,

    val listaColeccion: List<ProductoColeccionItem> = emptyList(),
    val listaVentas: List<PublicacionPerfilItem> = emptyList(),
    val listaCarrito: List<PublicacionPerfilItem> = emptyList()
)
