package com.example.snkrsapp.Domain

data class ModelListados(

    val esMiPerfil: Boolean = true,

    val cargandoColeccion: Boolean = false,
    val exitoColeccion: Boolean = false,

    val cargandoVentas: Boolean = false,
    val exitoVentas: Boolean = false,

    val cargandoCarrito: Boolean = false,
    val exitoCarrito: Boolean = false,
    val cargandoPedidos: Boolean = false,
    val cargandoIncidencias: Boolean = false,

    val cargandoPago: Boolean = false,
    val idPublicacionPidiendo: Int? = null,
    val idPedidoRespondiendo: Int? = null,
    val error: String? = null,
    val mensajeExito: String? = null,

    val listaColeccion: List<ProductoColeccionItem> = emptyList(),
    val listaVentas: List<PublicacionPerfilItem> = emptyList(),
    val listaCarrito: List<PublicacionPerfilItem> = emptyList(),
    val listaPedidosPendientes: List<PedidoRecibido> = emptyList(),
    val listaPedidosComprador: List<PedidoRecibido> = emptyList(),
    val listaIncidenciasUsuario: List<Incidencia> = emptyList()
)
