package com.example.snkrsapp.Data.RemoteData.PublicacionDao

data class AgregarPublicacionesSolicitud(
    val esMarcaNueva: Boolean,
    val idMarca: Int?,
    val nombreMarcaNueva: String?,
    val esProductoNuevo: Boolean,
    val idProducto: Int?,
    val nombreProductoNuevo: String?,
    val precio: Double,
    val talla: Double,
    val estado: String,
    val urlFoto: String,
    val fecha_publicacion: String,
    val esParaColeccion: Boolean
)

data class AgregarPublicacionRespuesta(
    val ok: Boolean,
    val message: String,
    val idMarca: Int?,
    val idProducto: Int?,
    val idPublicacion: Int?
)