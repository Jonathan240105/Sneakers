package com.example.snkrsapp.Data.RemoteData

import com.example.snkrsapp.Data.RemoteData.InicioSesion.InicioSesionDao
import com.example.snkrsapp.Data.RemoteData.Variables.Urls
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

object ApiSneakers {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Urls.urlSneakers)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val inicioSesionApi: InicioSesionDao by lazy { retrofit.create(InicioSesionDao::class.java) }
}