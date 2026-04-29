package com.example.snkrsapp.Domain

data class ModelPrincipal(
    val listaDeproductos: List<Producto> = emptyList(),
    val exitoProductos: Boolean = false,
    val exitoMarcas: Boolean = false,
    val cargandoProductos: Boolean = false,
    val cargandoMarcas: Boolean = false,
    val listaMarcas: List<Marca> = emptyList()
)