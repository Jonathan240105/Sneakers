package com.example.snkrsapp.Data.RemoteData.ProductoDao

data class TodosProductosRespuesta(
    val idProducto: Int? = null,
    val idMarca: Int = 0,
    val modelo: String = "",
    val precio: Int? = 0,
    val talla: Int? = 0,
    val uidVendedor: String? = "",
    val imagenUrl: String? = ""
)