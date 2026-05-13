package com.example.snkrsapp.Domain

import java.time.LocalDate
import java.time.YearMonth

data class ModelPantallaEventos(
    val listaEventos: List<Evento> = emptyList(),
    val exitoListaEventos: Boolean = false,
    val cargandoListaEventos: Boolean = false,
    val exitoEliminarEvento: Boolean = false,
    val cargandoEliminarEvento: Boolean = false,
    val exitoCrearEvento: Boolean = false,
    val cargandoCrearEvento: Boolean = false,
    var diaSeleccionado: Int = LocalDate.now().dayOfMonth,
    val mesSeleccionado: YearMonth = YearMonth.now(),

    val tituloNuevoEvento: String = "",
    val descripcionNuevoEvento: String = "",
    val fechaNuevoEvento: String = "",
    val fechaNuevoEventoBd : String = ""
)
