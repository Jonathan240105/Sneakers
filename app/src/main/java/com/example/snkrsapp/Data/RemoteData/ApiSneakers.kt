package com.example.snkrsapp.Data.RemoteData

import com.example.snkrsapp.Data.RemoteData.ActualizacionDao.ActualizacionDao
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.AutorizacionDao
import com.example.snkrsapp.Data.RemoteData.EventoDao.EventoDao
import com.example.snkrsapp.Data.RemoteData.ProductoDao.ProductosDao
import com.example.snkrsapp.Data.RemoteData.Variables.Urls
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object ApiSneakers {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Urls.urlSneakers)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    @Singleton
    fun provideInicioSesionApi(retrofit: Retrofit): AutorizacionDao =
        retrofit.create(AutorizacionDao::class.java)

    @Provides
    @Singleton
    fun provideProductoApi(retrofit: Retrofit): ProductosDao =
        retrofit.create(ProductosDao::class.java)

    @Provides
    @Singleton
    fun provideActualizacionApi(retrofit: Retrofit): ActualizacionDao =
        retrofit.create(ActualizacionDao::class.java)

    @Provides
    @Singleton
    fun provideEventoApi(retrofit: Retrofit): EventoDao =
        retrofit.create(EventoDao::class.java)
}