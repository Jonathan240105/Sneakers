package com.example.snkrsapp.Data.Repository.EventoRepository

import com.example.snkrsapp.Data.RemoteData.EventoDao.EventosSolicitud
import com.example.snkrsapp.Domain.EstadoEventosListado
import com.example.snkrsapp.Domain.Evento
import kotlinx.coroutines.flow.Flow

interface EventoRepository {
    suspend fun getEventos(uid:String?,token: String,fecha : String): Flow<List<Evento>>
    suspend fun eliminarEvento(token: String, id: Int): Flow<EstadoEventosListado>
    suspend fun crearEvento(token: String, body : EventosSolicitud,uid : String): Flow<EstadoEventosListado>
}