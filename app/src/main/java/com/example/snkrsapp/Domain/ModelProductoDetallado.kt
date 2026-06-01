package com.example.snkrsapp.Domain

data class ModelProductoDetallado(
    val productoSeleccionado: Producto = Producto(),
    val marcaSeleccionada: Marca = Marca(),
    val publicacionSeleccionada: Publicacion = Publicacion(),
    val listaPublicaciones: List<Publicacion> = emptyList(),
    val cargandoPublicaciones: Boolean = false
)
