package com.example.snkrsapp.Data.LocalData.Eventos

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.snkrsapp.Domain.Evento

@Entity(tableName = "Eventos")
data class EventoEntity(
    @PrimaryKey
    val idEvento: Int = 0,
    val titulo: String = "",
    val descripcion: String = "",
    val fechaEvento: String = "",
    val idOrganizador: Int = 0
)

fun EntityToEvento(evento: EventoEntity): Evento {
    return Evento(
        idEvento = evento.idEvento,
        titulo = evento.titulo,
        descripcion = evento.descripcion,
        fechaEvento = evento.fechaEvento,
        idOrganizador = evento.idOrganizador
    )
}

fun EventoToEntity(evento: Evento): EventoEntity {
    return EventoEntity(
        idEvento = evento.idEvento,
        titulo = evento.titulo,
        descripcion = evento.descripcion,
        fechaEvento = evento.fechaEvento,
        idOrganizador = evento.idOrganizador
    )
}
