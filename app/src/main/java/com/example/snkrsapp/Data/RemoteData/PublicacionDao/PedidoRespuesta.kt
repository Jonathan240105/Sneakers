package com.example.snkrsapp.Data.RemoteData.PublicacionDao

import com.example.snkrsapp.Domain.PedidoRecibido

data class PedidoPendienteRespuesta(
    val idPedido: Int?,
    val idPublicacion: Int?,
    val idVariante: Int?,
    val idColor: Int?,
    val uidComprador: String?,
    val nombreComprador: String?,
    val nombreVendedor: String?,
    val modelo: String?,
    val marca: String?,
    val urlFoto: String?,
    val color: String?,
    val nombreColor: String?,
    val talla: Double?,
    val cantidad: Int?,
    val precio: Double?,
    val precioUnidad: Double?,
    val estadoPedido: String?,
    val estado: String?,
    val fechaPedido: String?,
    val fechaEnvio: String?,
    val fechaLimiteConfirmacion: String?
)

data class PedidosPendientesRespuesta(
    val ok: Boolean,
    val pedidos: List<PedidoPendienteRespuesta>?
)

data class ResponderPedidoSolicitud(
    val uidComprador: String,
    val idPublicacion: Int,
    val accion: String
)

data class ConfirmarPedidoSolicitud(
    val idVariante: Int
)

data class ReportarPedidoSolicitud(
    val idPublicacion: Int,
    val tipo: String,
    val descripcion: String?,
    val urlImagen: String? = null
)

data class IncidenciaRespuesta(
    val idIncidencia: Int,
    val idPublicacion: Int?,
    val idVariante: Int?,
    val uidComprador: String?,
    val nombreComprador: String?,
    val nombreVendedor: String?,
    val modelo: String?,
    val color: String?,
    val talla: String?,
    val cantidad: String?,
    val tipo: String?,
    val descripcion: String?,
    val urlImagen: String?,
    val estado: String?,
    val fechaCreacion: String?
)

data class IncidenciasAdminRespuesta(
    val ok: Boolean,
    val incidencias: List<IncidenciaRespuesta>?
)

data class ResponderIncidenciaSolicitud(
    val idIncidencia: Int,
    val aceptar: Boolean
)

fun PedidoPendienteRespuesta.toDomain(): PedidoRecibido {
    return PedidoRecibido(
        idPedido = idPedido ?: idVariante ?: idPublicacion ?: 0,
        idPublicacion = idPublicacion ?: 0,
        idVariante = idVariante ?: 0,
        uidComprador = uidComprador.orEmpty(),
        nombreComprador = nombreComprador ?: "Comprador",
        nombreVendedor = nombreVendedor.orEmpty(),
        modelo = modelo ?: "Publicación",
        marca = marca.orEmpty(),
        urlFoto = urlFoto.orEmpty(),
        color = nombreColor ?: color ?: idColor?.let { "Color $it" } ?: "Sin color",
        talla = talla ?: 0.0,
        cantidad = cantidad ?: 1,
        precioUnidad = precioUnidad ?: precio ?: 0.0,
        estado = estadoPedido ?: estado ?: "pendiente",
        fechaPedido = fechaPedido,
        fechaEnvio = fechaEnvio,
        fechaLimiteConfirmacion = fechaLimiteConfirmacion
    )
}
