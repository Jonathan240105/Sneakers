package com.example.snkrsapp.Data.RemoteData.PublicacionDao

import com.example.snkrsapp.Data.LocalData.Publicaciones.PublicacionEntity

data class PublicacionesProductoRespuesta(
    val idPublicacion: Int,
    val idProducto: Int,
    val uidUsuario: String,
    val nombreUsuario: String,
    val precio: Double,
    val talla: Double,
    val estado: String,
    val urlFoto: String?,
    val fecha_publicacion: String,
    val disponible: String
)

fun PublicacionesProductoRespuesta.toEntity(): PublicacionEntity = PublicacionEntity(
    idPublicacion = idPublicacion,
    idProducto = idProducto,
    uidUsuario = uidUsuario,
    nombreUsuario = nombreUsuario,
    precio = precio,
    talla = talla,
    estado = estado,
    urlFoto = urlFoto ?: "",
    fechaPublicacion = fecha_publicacion,
    disponible = disponible == "disponible"
)