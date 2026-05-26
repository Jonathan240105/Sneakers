package com.example.snkrsapp.Data.LocalData.Publicaciones

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PublicacionLocalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun agregarPublicacion(publicacion: PublicacionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun agregarPublicaciones(publicaciones: List<PublicacionEntity>)

    @Query("Select * from publicaciones where idProducto = :idProducto ")
    suspend fun getPublicaciones(idProducto: Int): List<PublicacionEntity>

}