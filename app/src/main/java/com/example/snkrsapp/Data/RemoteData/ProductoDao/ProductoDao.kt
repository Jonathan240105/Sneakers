package com.example.snkrsapp.Data.RemoteData.ProductoDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.snkrsapp.Data.LocalData.Productos.ProductoEntity

@Dao
interface ProductoDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun añadirProducto(producto: ProductoEntity)

    @Query("Select * from Productos")
    suspend fun obtenerProductos(): List<ProductoEntity>

    @Query("Select * from Productos where idProducto = :idProducto")
    suspend fun obtenerProductoPorId(idProducto: Int): ProductoEntity?

    @Query("Delete from Productos where idProducto = :idProducto")
    suspend fun eliminarProductoPorId(idProducto: Int)
}