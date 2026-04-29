package com.example.snkrsapp.Data.RemoteData.ProductoDao

import java.time.LocalDate

data class MarcaRespuesta(
    val idMarca: Int = 0,
    val nombre: String = "",
    val paisOrigen: String = "",
    val fechaFundacion: String = "",
    val logoUrl: String = "",
    val web: String = ""
)
