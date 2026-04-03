package com.example.snkrsapp.Data.LocalData.UsuariosConectados

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.snkrsapp.Data.LocalData.Querys.Querys

@Dao
interface UsuariosConectadosDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun añadirUsuario(usuario: UsuarioEntity)

    @Query(Querys.obtenerUsuarioConectadoPorUID)
    suspend fun obtenerUsuarioPorUID(UID: String): UsuarioEntity?
}