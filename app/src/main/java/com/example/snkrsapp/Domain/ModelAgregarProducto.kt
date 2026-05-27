package com.example.snkrsapp.Domain

data class ModelAgregarProducto(

    val cargando: Boolean = false,
    val exito: Boolean = false,
    val mensajeError: String? = null,
    val mensajeExito: String? = null,

    val esColeccion : Boolean = false,
    val precioNuevaPublicacion: Double = 0.0,
    val tallaNuevaPublicacion: Double = 0.0,
    val estadoNuevaPublicacion: String = "",
    val urlImagenNuevaPublicacion: String = "",

    val idMarcaSeleccionada: Int = 0,
    val marcaSeleccionada: String = "",
    val nombreNuevaMarcaText: String = "",

    val idProductoSeleccionado: Int = 0,
    val modeloSeleccionado: String = "",
    val nombreNuevoProductoText: String = "",

    val textoBuscador: String = "",
    val sugerenciasModelos: List<ProductoItem> = emptyList()
)