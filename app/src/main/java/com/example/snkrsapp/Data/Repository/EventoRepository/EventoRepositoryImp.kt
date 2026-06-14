package com.example.snkrsapp.Data.Repository.EventoRepository

import com.example.snkrsapp.Data.LocalData.Eventos.EntityToEvento
import com.example.snkrsapp.Data.LocalData.Eventos.EventoEntity
import com.example.snkrsapp.Data.LocalData.Eventos.EventoLocalDao
import com.example.snkrsapp.Data.LocalData.Eventos.EventoToEntity
import com.example.snkrsapp.Data.RemoteData.EventoDao.CrearEventoSolicitud
import com.example.snkrsapp.Data.RemoteData.EventoDao.EliminarEventoSolicitud
import com.example.snkrsapp.Data.RemoteData.EventoDao.EventoDao
import com.example.snkrsapp.Data.RemoteData.EventoDao.EventosSolicitud
import com.example.snkrsapp.Domain.EstadoEliminarEventos
import com.example.snkrsapp.Domain.EstadoEventosListado
import com.example.snkrsapp.Domain.Evento
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EventoRepositoryImp @Inject constructor(
    private val eventoDao: EventoDao, private val eventoLocalDao: EventoLocalDao
) : EventoRepository {

    override suspend fun getEventos(
        uid: String?,
        token: String,
        fecha: String
    ): Flow<List<Evento>> = flow {

        val eventosLocales = eventoLocalDao.listarEventos(fecha, uid)

        if (eventosLocales.isNotEmpty()) {
            emit(eventosLocales.map { EntityToEvento(it) })
        }
        try {

            val respuesta = eventoDao.getEventos("Bearer $token", fecha)

            if (respuesta.isSuccessful) {
                eventoLocalDao.insertarEventos(respuesta.body()?.map { EventoToEntity(it) }
                    ?: emptyList())
                emit(eventoLocalDao.listarEventos(fecha, uid).map { EntityToEvento(it) })
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    override suspend fun eliminarEvento(
        token: String, id: Int
    ): Flow<EstadoEventosListado> = flow {

        try {
            val respuesta = eventoDao.eliminarEvento("Bearer $token", id)

            if (respuesta.isSuccessful && respuesta.body()?.ok == true) {
                eventoLocalDao.eliminarEvento(id)

                emit(EstadoEventosListado(true, "Evento eliminado correctamente"))
            } else {
                val mensaje = respuesta.body()?.message ?: "No autorizado"
                emit(EstadoEventosListado(false, mensaje))
            }
        } catch (e: Exception) {
            emit(EstadoEventosListado(false, "Error de conexión: ${e.message}"))
        }
    }

    override suspend fun crearEvento(
        token: String,
        body: EventosSolicitud,
        uid: String
    ): Flow<EstadoEventosListado> = flow {
        val respuesta = eventoDao.crearEvento("Bearer $token", body)

        if (respuesta.isSuccessful && respuesta.body()?.ok == true) {
            eventoLocalDao.insertarEvento(
                EventoEntity(
                    respuesta.body()?.idAgregado ?: 0,
                    body.titulo,
                    body.descripcion,
                    body.fechaEvento,
                    uid
                )
            )
            emit(EstadoEventosListado(true, "Evento creado con éxito"))
        } else {
            val msg = respuesta.body()?.message ?: "Error al crear el evento"
            emit(EstadoEventosListado(false, msg))
        }
    }

    override suspend fun getEventosAdmin(token: String): Flow<List<Evento>> = flow {
        try {

            val respuesta = eventoDao.obtenerEventosAdmin("Bearer $token")

            if (respuesta.isSuccessful) {
                emit(respuesta.body()?.map { EntityToEvento(EventoToEntity(it)) } ?: emptyList())
            } else {
                emit(emptyList())
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            emit(emptyList())
        }
    }

    override suspend fun eliminarEventos(
        token: String,
        ids: List<Int>
    ): Flow<EstadoEliminarEventos> = flow {

        try {
            emit(EstadoEliminarEventos.Cargando)
            val respuesta = eventoDao.eliminarEventos("Bearer $token", EliminarEventoSolicitud(ids))

            if (respuesta.isSuccessful && respuesta.body()?.ok == true) {
                emit(EstadoEliminarEventos.Exito("Eventos eliminados correctamente"))
            } else {
                emit(EstadoEliminarEventos.Error("Error al eliminar los eventos"))
            }
        } catch (e: Exception) {
            emit(EstadoEliminarEventos.Error("Error de conexión: ${e.message}"))
        }
    }

    override suspend fun crearEvento(
        token: String,
        body: CrearEventoSolicitud
    ): Flow<EstadoEventosListado> = flow {
        try {
            val respuesta = eventoDao.crearEventoGLobal("Bearer $token", body)

            if (respuesta.isSuccessful) {
                emit(EstadoEventosListado(true, "Evento creado con éxito"))
            } else {
                val msg = respuesta.body()?.message ?: "Error al crear el evento"
                emit(EstadoEventosListado(false, msg))
            }
        } catch (e: Exception) {
            emit(EstadoEventosListado(false, "Error de conexión: ${e.message}"))
        }

    }

}