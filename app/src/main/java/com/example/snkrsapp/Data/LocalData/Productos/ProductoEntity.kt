package com.example.snkrsapp.Data.LocalData.Productos

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.snkrsapp.Data.RemoteData.ProductoDao.TodosProductosRespuesta


@Entity(tableName = "Productos")
data class ProductoEntity(
    @PrimaryKey val idProducto: Int = 0,
    val idMarca: Int = 0,
    val modelo: String = "",
    val precio: Int = 0,
    val talla: Int = 0,
    val uidVendedor: String = "",
)

fun ProductoEntityToProducto(producto: ProductoEntity): TodosProductosRespuesta {
    return TodosProductosRespuesta(
        idProducto = producto.idProducto,
        idMarca = producto.idMarca,
        modelo = producto.modelo,
        precio = producto.precio,
        talla = producto.talla,
        uidVendedor = producto.uidVendedor
    )
}

fun ProductoToProductoEntity(producto: TodosProductosRespuesta): ProductoEntity {
    return ProductoEntity(
        idProducto = producto.idProducto ?: 0,
        idMarca = producto.idMarca,
        modelo = producto.modelo,
        precio = producto.precio,
        talla = producto.talla,
        uidVendedor = producto.uidVendedor
    )

}
