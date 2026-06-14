package com.example.snkrsapp.Data.LocalData.PublicacionPropia

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.snkrsapp.Data.LocalData.Perfil.CarritoEntity
import com.example.snkrsapp.Data.LocalData.Perfil.ColeccionEntity
import com.example.snkrsapp.Data.LocalData.Perfil.VentasEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PublicacionesPropiasLocalDao {

    @Query("SELECT * FROM Carrito WHERE uidUsuario = :uidUsuario")
    fun obtenerCarritoLocal(uidUsuario: String): Flow<List<CarritoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCarritoLocal(items: List<CarritoEntity>)

    @Query("DELETE FROM Carrito WHERE uidUsuario = :uidUsuario")
    suspend fun vaciarCarritoLocal(uidUsuario: String)

    @Query("SELECT * FROM coleccion WHERE uidUsuario = :uidUsuario")
    fun obtenerColeccionLocal(uidUsuario: String): Flow<List<ColeccionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarColeccionLocal(items: List<ColeccionEntity>)

    @Query("DELETE FROM coleccion WHERE uidUsuario = :uidUsuario")
    suspend fun vaciarColeccionLocal(uidUsuario: String)


    @Query("SELECT * FROM Ventas WHERE uidUsuario = :uidUsuario")
    fun obtenerVentasLocal(uidUsuario: String): Flow<List<VentasEntity>>

    @Query("SELECT * FROM Ventas WHERE uidUsuario = :uidUsuario AND estado = 'disponible'")
    fun obtenerVentasDisponiblesLocal(uidUsuario: String): Flow<List<VentasEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarVentasLocal(items: List<VentasEntity>)

    @Query("DELETE FROM Ventas WHERE uidUsuario = :uidUsuario")
    suspend fun vaciarVentasLocal(uidUsuario: String)
}