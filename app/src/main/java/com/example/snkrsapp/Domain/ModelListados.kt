package com.example.snkrsapp.Domain

data class ModelListados(

    val esMiPerfil: Boolean = true,

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