package com.example.snkrsapp.Domain

data class ModelAgregarProducto(

    val cargando: Boolean = false,
    val exito: Boolean = false,
    val mensajeError: String? = null,
    val mensajeExito: String? = null,

    val esColeccion : Boolean = false,
    val precioNuevaPublicacion: Double = 0.0,
    val tallaNuevaPublicacion: Double = 0.0,
    val urlImagenNuevaPublicacion: String = "",
    val coloresDisponibles: List<ColorPublicacion> = emptyList(),
    val variantes: List<VarianteNuevaPublicacion> = listOf(VarianteNuevaPublicacion()),

    val idMarcaSeleccionada: Int = 0,
    val marcaSeleccionada: String = "",
    val nombreNuevaMarcaText: String = "",

    val idProductoSeleccionado: Int = 0,
    val modeloSeleccionado: String = "",
    val nombreNuevoProductoText: String = "",

    val textoBuscador: String = "",
    val sugerenciasModelos: List<ProductoItem> = emptyList(),

    val cargandoImagen : Boolean? = false,
    val errorImagen : String? = null,
)

data class VarianteNuevaPublicacion(
    val idColor: Int = 0,
    val nombreColor: String = "",
    val hexColor: String? = null,
    val talla: Double = 0.0,
    val cantidadDisponible: Int = 1,
    val urlFoto: String = "",
    val cargandoImagen: Boolean = false,
    val errorImagen: String? = null
)
