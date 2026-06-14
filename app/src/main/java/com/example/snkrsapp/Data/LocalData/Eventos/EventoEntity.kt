package com.example.snkrsapp.Data.LocalData.Eventos

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.snkrsapp.Data.RemoteData.EventoDao.EventosRespuesta
import com.example.snkrsapp.Data.RemoteData.EventoDao.EventosSolicitud
import com.example.snkrsapp.Domain.Evento

@Entity(tableName = "Eventos")
data class EventoEntity(
    @PrimaryKey val idEvento: Int = 0,
    val titulo: String = "",
    val descripcion: String = "",
    val fechaEvento: String = "",
    val idOrganizador: String? = ""
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

fun EventoToEntity(evento: EventosRespuesta): EventoEntity {
    return EventoEntity(
        idEvento = evento.idEvento,
        titulo = evento.titulo,
        descripcion = evento.descripcion,
        fechaEvento = evento.fechaEvento,
        idOrganizador = evento.idOrganizador
    )
}
