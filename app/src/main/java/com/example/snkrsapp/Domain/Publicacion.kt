package com.example.snkrsapp.Domain

data class Publicacion(
    val idPublicacion: Int = 0,
    val idProducto: Int = 0,
    val uidUsuario: String = "",
    val nombreUsuario: String? = "",
    val precio: Double = 0.0,
    val talla: Double = 0.0,
    val estado: String = "",
    val urlFoto: String = "",
    val fechaPublicacion: String = "",
    val stock: Int = 1,
    val variantes: List<VariantePublicacion> = emptyList()
)

data class VariantePublicacion(
    val idVariante: Int = 0,
    val idPublicacion: Int = 0,
    val idColor: Int = 0,
    val color: String = "",
    val hexColor: String? = null,
    val talla: Double = 0.0,
    val cantidadDisponible: Int = 0,
    val urlFoto: String = ""
)
