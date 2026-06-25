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
    val variantes: List<VariantePublicacionSolicitud>,
    val esParaColeccion: Boolean
)

data class VariantePublicacionSolicitud(
    val idColor: Int,
    val talla: Double,
    val cantidadDisponible: Int,
    val urlFoto: String
)

data class AgregarPublicacionRespuesta(
    val ok: Boolean,
    val message: String,
    val idMarca: Int?,
    val idProducto: Int?,
    val idPublicacion: Int?
)
