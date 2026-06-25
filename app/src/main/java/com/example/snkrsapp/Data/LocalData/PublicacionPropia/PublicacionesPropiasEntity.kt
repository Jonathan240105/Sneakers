package com.example.snkrsapp.Data.LocalData.Perfil

import androidx.room.Entity
import com.example.snkrsapp.Data.RemoteData.PublicacionDao.PublicacionCarritoRespuesta
import com.example.snkrsapp.Data.RemoteData.PublicacionDao.PublicacionPropiaListado
import com.example.snkrsapp.Domain.ProductoColeccionItem
import com.example.snkrsapp.Domain.PublicacionPerfilItem

@Entity(tableName = "Carrito", primaryKeys = ["uidUsuario", "idVariante"])
data class CarritoEntity(
    val uidUsuario: String,
    val idCarrito: Int?,
    val idPublicacion: Int,
    val idVariante: Int,
    val idProducto: Int,
    val idMarca: Int,
    val modelo: String,
    val marca: String,
    val precio: Double,
    val talla: Double,
    val urlFoto: String,
    val estado: String?,
    val idColor: Int,
    val nombreColor: String,
    val cantidad: Int,
    val estadoPedido: String
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
        idCarrito = this.idCarrito,
        idPublicacion = this.idPublicacion,
        idVariante = this.idVariante ?: 0,
        idProducto = this.idProducto,
        idMarca = this.idMarca,
        modelo = this.modelo,
        marca = this.marca,
        precio = this.precio,
        talla = this.talla,
        urlFoto = this.urlFoto,
        estado = this.estado,
        idColor = this.idColor ?: 0,
        nombreColor = this.nombreColor ?: this.color ?: "",
        cantidad = this.cantidad ?: 1,
        estadoPedido = this.estadoPedido ?: this.estadoCarrito ?: "carrito"
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
        idCarrito = this.idCarrito,
        idPublicacion = this.idPublicacion,
        idVariante = this.idVariante,
        idProducto = this.idProducto,
        idMarca = this.idMarca,
        modelo = this.modelo,
        marca = this.marca,
        precio = this.precio,
        talla = this.talla,
        urlFoto = this.urlFoto,
        estado = this.estado,
        idColor = this.idColor,
        nombreColor = this.nombreColor,
        cantidad = this.cantidad,
        estadoPedido = this.estadoPedido
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
