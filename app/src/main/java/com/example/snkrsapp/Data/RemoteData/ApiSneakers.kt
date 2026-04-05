package com.example.snkrsapp.Data.RemoteData

import com.example.snkrsapp.Data.RemoteData.InicioSesion.InicioSesionDao
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
    fun provideInicioSesionApi(retrofit: Retrofit): InicioSesionDao =
        retrofit.create(InicioSesionDao::class.java)
}