package com.example.snkrsapp.Data.di

import android.content.Context
import androidx.room.Room
import com.example.snkrsapp.Data.LocalData.BaseDatos
import com.example.snkrsapp.Data.LocalData.Eventos.EventoLocalDao
import com.example.snkrsapp.Data.LocalData.Marcas.MarcaLocalDao
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuariosConectadosDao
import com.example.snkrsapp.Data.LocalData.Productos.ProductoLocalDao
import com.example.snkrsapp.Data.LocalData.Publicaciones.PublicacionLocalDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuloBaseDatos {

    @Provides
    @Singleton
    fun CreadorBaseDatos(@ApplicationContext context: Context): BaseDatos {
        return Room.databaseBuilder(
            context,
            BaseDatos::class.java,
            "Base de datos aplicación sneakers"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideInicioSesionDao(db: BaseDatos): UsuariosConectadosDao {
        return db.UsuariosConectadosDao()
    }

    @Provides
    fun provideProductoDao(db: BaseDatos): ProductoLocalDao {
        return db.ProductoDao()
    }

    @Provides
    fun provideMarcaDao(db: BaseDatos): MarcaLocalDao {
        return db.MarcaDao()
    }

    @Provides
    fun provideEventoDao(db: BaseDatos): EventoLocalDao {
        return db.EventoLocalDao()
    }

    @Provides
    fun providePublicacionDao(db: BaseDatos): PublicacionLocalDao {
        return db.PublicacionLocalDao()
    }

}