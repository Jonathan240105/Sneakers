package com.example.snkrsapp.Data.di

import android.content.Context
import androidx.room.Room
import com.example.snkrsapp.Data.LocalData.BaseDatos
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuariosConectadosDao
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
}