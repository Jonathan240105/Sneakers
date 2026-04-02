package com.example.snkrsapp.Data.LocalData

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuarioEntity
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuariosConectadosDao

@Database(
    entities = [UsuarioEntity::class],
    version = 0,
    exportSchema = false
)
abstract class BaseDatos : RoomDatabase() {
    abstract fun UsuariosConectadosDao(): UsuariosConectadosDao

}