package com.example.snkrsapp.Domain

data class ModelPrincipal(
    val listaDeproductos: List<Producto> = emptyList(),
    val exito: Boolean = false,
    val cargando: Boolean = false,
    val listaMarcas: List<String> = emptyList()
)