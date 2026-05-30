package com.example.snkrsapp.Domain

data class ModelPrincipal(
    val listaDeproductos: List<Producto> = emptyList(),
    val exitoProductos: Boolean = false,
    val exitoMarcas: Boolean = false,
    val cargandoProductos: Boolean = false,
    val cargandoMarcas: Boolean = false,
    val listaMarcas: List<Marca> = emptyList(),

    val esListaFiltrada: Boolean = false,
    val esBusquedaTexto: Boolean = false,
    val textoBusquedaActual: String = "",

    val minPrecioSeleccionado: Double? = null,
    val maxPrecioSeleccionado: Double? = null,
    val tallaSeleccionado: Double? = null,
    val marcasSeleccionadas: List<Int>? = null,

    val minPrecio: Int? = null,
    val maxPrecio: Int? = null,
    val talla: Double? = null,
    val marcas: List<Int>? = null,
)