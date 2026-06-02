package com.example.snkrsapp.Data.LocalData.Publicaciones

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.snkrsapp.Data.RemoteData.PublicacionDao.PublicacionesRespuesta
import com.example.snkrsapp.Domain.Publicacion

@Entity(tableName = "Publicaciones")
data class PublicacionEntity(
    @PrimaryKey val idPublicacion: Int,
    val idProducto: Int,
    val uidUsuario: String,
    val nombreUsuario: String?="",
    val precio: Double,
    val talla: Double,
    val estado: String,
    val urlFoto: String,
    val fechaPublicacion: String,
    val disponible: Boolean
)

fun EntityToPublicacion(publicacion: PublicacionEntity): Publicacion {
    return Publicacion(
        idPublicacion = publicacion.idPublicacion,
        idProducto = publicacion.idProducto,
        uidUsuario = publicacion.uidUsuario,
        nombreUsuario = publicacion.nombreUsuario,
        precio = publicacion.precio,
        talla = publicacion.talla,
        estado = publicacion.estado,
        urlFoto = publicacion.urlFoto,
        fechaPublicacion = publicacion.fechaPublicacion,
        disponible = publicacion.disponible
    )
}

fun PublicacionToEntity(publicacion: PublicacionesRespuesta): PublicacionEntity {
    return PublicacionEntity(
        idPublicacion = publicacion.idPublicacion,
        idProducto = publicacion.idProducto,
        uidUsuario = publicacion.uidUsuario,
        precio = publicacion.precio,
        talla = publicacion.talla,
        estado = publicacion.estado,
        urlFoto = publicacion.urlFoto,
        fechaPublicacion = publicacion.fechaPublicacion,
        disponible = publicacion.disponible
    )
}