package com.example.snkrsapp.Data.LocalData.Perfil

import androidx.room.Entity
import com.example.snkrsapp.Data.RemoteData.PublicacionDao.PublicacionCarritoRespuesta
import com.example.snkrsapp.Data.RemoteData.PublicacionDao.PublicacionPropiaListado
import com.example.snkrsapp.Domain.ProductoColeccionItem
import com.example.snkrsapp.Domain.PublicacionPerfilItem

@Entity(tableName = "Carrito", primaryKeys = ["uidUsuario", "idPublicacion"])
data class CarritoEntity(
    val uidUsuario: String,
    val idPublicacion: Int,
    val idProducto: Int,
    val idMarca: Int,
    val modelo: String,
    val marca: String,
    val precio: Double,
    val talla: Double,
    val urlFoto: String,
    val estado: String?
)

@Entity(tableName = "Coleccion", primaryKeys = ["uidUsuario", "idProducto"])
data class ColeccionEntity(
    val uidUsuario: String,
    val idProducto: Int,
    val idMarca: Int,
    val modelo: String,
    val marca: String,
    val urlFoto: String,
    val precio: Double
)

@Entity(tableName = "Ventas", primaryKeys = ["uidUsuario", "idPublicacion"])
data class VentasEntity(
    val uidUsuario: String,
    val idPublicacion: Int,
    val idProducto: Int,
    val idMarca: Int,
    val modelo: String,
    val marca: String,
    val precio: Double,
    val talla: Double,
    val urlFoto: String,
    val estado: String?
)

fun PublicacionPropiaListado.toCarritoEntity(uidUsuario: String): CarritoEntity {
    return CarritoEntity(
        uidUsuario = uidUsuario,
        idPublicacion = this.idPublicacion,
        idProducto = this.idProducto,
        idMarca = this.idMarca,
        modelo = this.modelo,
        marca = this.marca,
        precio = this.precio,
        talla = this.talla,
        urlFoto = this.urlFoto,
        estado = this.estado
    )
}

fun PublicacionPropiaListado.toVentasEntity(uidUsuario: String): VentasEntity {
    return VentasEntity(
        uidUsuario = uidUsuario,
        idPublicacion = this.idPublicacion,
        idProducto = this.idProducto,
        idMarca = this.idMarca,
        modelo = this.modelo,
        marca = this.marca,
        precio = this.precio,
        talla = this.talla,
        urlFoto = this.urlFoto,
        estado = this.estado
    )
}

fun PublicacionCarritoRespuesta.toColeccionEntity(uidUsuario: String): ColeccionEntity {
    return ColeccionEntity(
        uidUsuario = uidUsuario,
        idProducto = this.idProducto,
        idMarca = this.idMarca,
        modelo = this.modelo,
        marca = this.marca,
        urlFoto = this.urlFoto,
        precio = this.precio
    )
}


fun CarritoEntity.toDomain(): PublicacionPerfilItem {
    return PublicacionPerfilItem(
        idPublicacion = this.idPublicacion,
        idProducto = this.idProducto,
        idMarca = this.idMarca,
        modelo = this.modelo,
        marca = this.marca,
        precio = this.precio,
        talla = this.talla,
        urlFoto = this.urlFoto,
        estado = this.estado
    )
}

fun VentasEntity.toDomain(): PublicacionPerfilItem {
    return PublicacionPerfilItem(
        idPublicacion = this.idPublicacion,
        idProducto = this.idProducto,
        idMarca = this.idMarca,
        modelo = this.modelo,
        marca = this.marca,
        precio = this.precio,
        talla = this.talla,
        urlFoto = this.urlFoto,
        estado = this.estado
    )
}

fun ColeccionEntity.toDomain(): ProductoColeccionItem {
    return ProductoColeccionItem(
        idProducto = this.idProducto,
        idMarca = this.idMarca,
        modelo = this.modelo,
        marca = this.marca,
        urlFoto = this.urlFoto,
        precio = this.precio
    )
}
