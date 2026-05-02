package com.example.snkrsapp.Domain

data class ModelProductoDetallado(
    val productoSeleccionado: Producto = Producto(),
    val marcaSeleccionada : Marca = Marca()
)
