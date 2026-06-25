package com.example.snkrsapp.Domain

data class ModelProductoDetallado(
    val productoSeleccionado: Producto = Producto(),
    val marcaSeleccionada: Marca = Marca(),
    val publicacionSeleccionada: Publicacion = Publicacion(),
    val listaPublicaciones: List<Publicacion> = emptyList(),
    val coloresPublicacion: List<ColorPublicacion> = emptyList(),
    val cargandoColores: Boolean = false,
    val cargandoPublicaciones: Boolean = false,
    val mensaje : String = ""
)
