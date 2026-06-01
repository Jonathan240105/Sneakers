package com.example.snkrsapp.Data.LocalData

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.snkrsapp.Data.LocalData.Eventos.EventoEntity
import com.example.snkrsapp.Data.LocalData.Eventos.EventoLocalDao
import com.example.snkrsapp.Data.LocalData.Marcas.MarcaEntity
import com.example.snkrsapp.Data.LocalData.Marcas.MarcaLocalDao
import com.example.snkrsapp.Data.LocalData.Perfil.CarritoEntity
import com.example.snkrsapp.Data.LocalData.Perfil.ColeccionEntity
import com.example.snkrsapp.Data.LocalData.Perfil.VentasEntity
import com.example.snkrsapp.Data.LocalData.Productos.ProductoEntity
import com.example.snkrsapp.Data.LocalData.Productos.ProductoLocalDao
import com.example.snkrsapp.Data.LocalData.PublicacionPropia.PublicacionesPropiasLocalDao
import com.example.snkrsapp.Data.LocalData.Publicaciones.PublicacionEntity
import com.example.snkrsapp.Data.LocalData.Publicaciones.PublicacionLocalDao
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuarioEntity
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuariosConectadosDao

@Database(
    entities = [UsuarioEntity::class, ProductoEntity::class, MarcaEntity::class, EventoEntity::class, PublicacionEntity::class, CarritoEntity::class, VentasEntity::class, ColeccionEntity::class],
    version = 13,
    exportSchema = false
)
abstract class BaseDatos : RoomDatabase() {
    abstract fun UsuariosConectadosDao(): UsuariosConectadosDao
    abstract fun ProductoDao(): ProductoLocalDao
    abstract fun MarcaDao(): MarcaLocalDao
    abstract fun EventoLocalDao(): EventoLocalDao
    abstract fun PublicacionLocalDao(): PublicacionLocalDao
    abstract fun PublicacionesPropiasLocalDao(): PublicacionesPropiasLocalDao
}