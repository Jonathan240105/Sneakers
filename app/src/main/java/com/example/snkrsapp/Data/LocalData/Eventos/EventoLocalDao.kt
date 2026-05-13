package com.example.snkrsapp.Data.LocalData.Eventos

import androidx.compose.ui.text.style.LineBreak
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EventoLocalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEventos(listaEventos: List<EventoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEvento(evento: EventoEntity)

    @Query("Delete from Eventos where idEvento = :idEvento")
    suspend fun eliminarEvento(idEvento: Int)

    @Query("Select * from eventos where date(fechaEvento) = date(:fecha) and (idOrganizador = :uid or idOrganizador is null)")
    suspend fun listarEventos(fecha: String,uid : String?): List<EventoEntity>
}
