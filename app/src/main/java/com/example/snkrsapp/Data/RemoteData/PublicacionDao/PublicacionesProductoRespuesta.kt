package com.example.snkrsapp.Data.RemoteData.PublicacionDao

import com.example.snkrsapp.Data.LocalData.Publicaciones.PublicacionEntity
import com.example.snkrsapp.Domain.Publicacion
import com.example.snkrsapp.Domain.VariantePublicacion

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
    val stock: Int = 1,
    val variantes: List<VariantePublicacionRespuesta>? = emptyList()
)

data class VariantePublicacionRespuesta(
    val idVariante: Int,
    val idPublicacion: Int,
    val idColor: Int,
    val color: String,
    val hexColor: String?,
    val talla: Double,
    val cantidadDisponible: Int,
    val urlFoto: String?
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
    stock = stock
)

fun PublicacionesProductoRespuesta.toDomain(): Publicacion = Publicacion(
    idPublicacion = idPublicacion,
    idProducto = idProducto,
    uidUsuario = uidUsuario,
    nombreUsuario = nombreUsuario,
    precio = precio,
    talla = talla,
    estado = estado,
    urlFoto = urlFoto ?: variantes?.firstOrNull()?.urlFoto.orEmpty(),
    fechaPublicacion = fecha_publicacion,
    stock = variantes?.sumOf { it.cantidadDisponible } ?: stock,
    variantes = variantes.orEmpty().map {
        VariantePublicacion(
            idVariante = it.idVariante,
            idPublicacion = it.idPublicacion,
            idColor = it.idColor,
            color = it.color,
            hexColor = it.hexColor,
            talla = it.talla,
            cantidadDisponible = it.cantidadDisponible,
            urlFoto = it.urlFoto.orEmpty()
        )
    }
)
