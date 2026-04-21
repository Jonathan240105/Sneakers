package com.example.snkrsapp.TestInicioSesion

import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.AutorizacionDao
import com.example.snkrsapp.Data.Repository.UsuarioRepository.UsuarioRepositoryImp
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class InicioSesionUsuarioRepositoryTest {
    val api = mockk<AutorizacionDao>()
    val iniSesionDao = mockk<AutorizacionDao>()

    private val repository = UsuarioRepositoryImp(iniSesionDao)

    @Test
    fun a() = runTest{
        repository.iniciarSesion("","")
    }
}