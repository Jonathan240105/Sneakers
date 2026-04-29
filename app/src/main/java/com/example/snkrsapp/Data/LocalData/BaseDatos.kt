package com.example.snkrsapp.Data.LocalData

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.snkrsapp.Data.LocalData.Marcas.MarcaEntity
import com.example.snkrsapp.Data.LocalData.Marcas.MarcaLocalDao
import com.example.snkrsapp.Data.LocalData.Productos.ProductoEntity
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuarioEntity
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuariosConectadosDao
import com.example.snkrsapp.Data.LocalData.Productos.ProductoLocalDao

@Database(
    entities = [UsuarioEntity::class, ProductoEntity::class, MarcaEntity::class],
    version = 3,
    exportSchema = false
)
abstract class BaseDatos : RoomDatabase() {
    abstract fun UsuariosConectadosDao(): UsuariosConectadosDao

    abstract fun ProductoDao(): ProductoLocalDao
    abstract fun MarcaDao(): MarcaLocalDao
}