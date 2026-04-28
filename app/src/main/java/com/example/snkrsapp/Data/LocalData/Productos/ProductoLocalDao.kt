package com.example.snkrsapp.Data.LocalData.Productos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductoLocalDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun añadirProducto(producto: ProductoEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertarLista(productos: List<ProductoEntity>)

    @Query("Select * from Productos")
    suspend fun obtenerProductos(): List<ProductoEntity>

    @Query("Select * from Productos limit :limite offset :salto")
    suspend fun obtenerPaginaProductos(limite: Int, salto: Int): List<ProductoEntity>

    @Query("Select * from Productos where idProducto = :idProducto")
    suspend fun obtenerProductoPorId(idProducto: Int): ProductoEntity?

    @Query("Delete from Productos where idProducto = :idProducto")
    suspend fun eliminarProductoPorId(idProducto: Int)
}