package com.example.snkrsapp.Data.RemoteData.ProductoDao

data class AgregarProductoRespuesta(
    val ok: Boolean,
    val message: String,
    val idAgregado: Int
)

data class AgregarProductoSolicitud(
    val idMarca: Int,
    val modelo: String,
    val precio: Int,
    val talla: Int,
    val uidVendedor: String,
    val imagenUrl: String
)