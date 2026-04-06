package com.example.snkrsapp.TestInicioSesion

import com.example.snkrsapp.Data.RemoteData.InicioSesion.InicioSesionDao
import com.example.snkrsapp.Data.Repository.Repository
import com.example.snkrsapp.Data.Repository.RepositoryImp
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class InicioSesionRepositoryTest {
    val api = mockk<InicioSesionDao>()
    val iniSesionDao = mockk<InicioSesionDao>()

    private val repository = RepositoryImp(iniSesionDao)

    @Test
    fun a() = runTest{
        repository.iniciarSesion("","")
    }
}