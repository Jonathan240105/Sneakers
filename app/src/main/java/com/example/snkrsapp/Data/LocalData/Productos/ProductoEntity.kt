package com.example.snkrsapp.Data.LocalData.Productos

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.snkrsapp.Data.RemoteData.ProductoDao.TodosProductosRespuesta
import com.example.snkrsapp.Domain.Producto


@Entity(tableName = "Productos")
data class ProductoEntity(
    @PrimaryKey val idProducto: Int = 0,
    val idMarca: Int = 0,
    val modelo: String = "",
    val precio: Double = 0.0,
    val imagenUrl: String = ""
)

fun ProductoEntityToProducto(producto: ProductoEntity): Producto {
    return Producto(
        idProducto = producto.idProducto,
        idMarca = producto.idMarca,
        modelo = producto.modelo,
        precio = producto.precio,
        imagenUrl = producto.imagenUrl
    )
}

fun ProductoRespuestaToProductoEntity(producto: TodosProductosRespuesta): ProductoEntity {
    return ProductoEntity(
        idProducto = producto.idProducto ?: 0,
        idMarca = producto.idMarca,
        modelo = producto.modelo,
        precio = producto.precio?:0.0,
        imagenUrl = producto.imagenUrl?:""
    )

}
