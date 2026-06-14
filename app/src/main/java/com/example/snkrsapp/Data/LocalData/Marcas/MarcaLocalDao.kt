package com.example.snkrsapp.Data.LocalData.Marcas

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MarcaLocalDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertarListaMarcas(marcas: List<MarcaEntity>)

    @Query("Select * from Marcas")
    suspend fun obtenerMarcas(): List<MarcaEntity>

    @Query("Select * from Marcas where idMarca = :idMarca")
    suspend fun obtenerMarcaPorId(idMarca: Int): MarcaEntity
}