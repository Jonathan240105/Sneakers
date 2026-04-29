package com.example.snkrsapp.Data.LocalData.Marcas

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.snkrsapp.Data.RemoteData.ProductoDao.MarcaRespuesta
import com.example.snkrsapp.Domain.Marca
import java.time.LocalDate

@Entity(tableName = "Marcas")
data class MarcaEntity(
    @PrimaryKey val idMarca: Int = 0,
    val nombre: String = "",
    val paisOrigen: String = "",
    val fechaFundacion: String = "",
    val logoUrl: String = "",
    val web: String = ""
)

fun MarcaRespuestaToEntity(marca: MarcaRespuesta): MarcaEntity {
    return MarcaEntity(
        idMarca = marca.idMarca,
        nombre = marca.nombre,
        paisOrigen = marca.paisOrigen,
        fechaFundacion = marca.fechaFundacion,
        logoUrl = marca.logoUrl,
        web = marca.web
    )
}

fun EntityToMarca(marca: MarcaEntity): Marca {
    return Marca(
        idMarca = marca.idMarca,
        nombre = marca.nombre,
        logoUrl = marca.logoUrl
    )
}