package com.example.snkrsapp.Data.LocalData

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.snkrsapp.Data.LocalData.Eventos.EventoEntity
import com.example.snkrsapp.Data.LocalData.Eventos.EventoLocalDao
import com.example.snkrsapp.Data.LocalData.Marcas.MarcaEntity
import com.example.snkrsapp.Data.LocalData.Marcas.MarcaLocalDao
import com.example.snkrsapp.Data.LocalData.Productos.ProductoEntity
import com.example.snkrsapp.Data.LocalData.Productos.ProductoLocalDao
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuarioEntity
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuariosConectadosDao

@Database(
    entities = [UsuarioEntity::class, ProductoEntity::class, MarcaEntity::class, EventoEntity::class],
    version = 9,
    exportSchema = false
)
abstract class BaseDatos : RoomDatabase() {
    abstract fun UsuariosConectadosDao(): UsuariosConectadosDao
    abstract fun ProductoDao(): ProductoLocalDao
    abstract fun MarcaDao(): MarcaLocalDao
    abstract fun EventoLocalDao(): EventoLocalDao
}