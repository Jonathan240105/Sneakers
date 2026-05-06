package com.example.snkrsapp.TestPerfil

import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuarioEntity
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuariosConectadosDao
import com.example.snkrsapp.Data.RemoteData.ActualizacionDao.ActualizacionDao
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.AutorizacionDao
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario
import com.example.snkrsapp.Data.Repository.UsuarioRepository.UsuarioRepositoryImp
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class PerfilRepositoryTest {

    private val usuarioDao = mockk<UsuariosConectadosDao>(relaxed = true)
    private val autDao = mockk<AutorizacionDao>(relaxed = true)
    private val actuDao = mockk<ActualizacionDao>(relaxed = true)
    private val repository = UsuarioRepositoryImp(autDao, usuarioDao,actuDao)

    @Test
    fun `cuando hay datos en bd local no debe llamar a la api`(): Unit = runTest {
        val token = "abc"
        val userLocal = UsuarioEntity(UID = "123", nombreUsuario = "Erick")
        coEvery { usuarioDao.obtenerUsuarioPorUID(token) } returns userLocal

        repository.traerPerfil(token).collect{}

        coVerify(exactly = 0) { autDao.getPerfil(any()) }
    }

    @Test
    fun `cuando BD local es null SI debe llamar a la API y guardar`() = runTest {
        val token = "abc"
        coEvery { usuarioDao.obtenerUsuarioPorUID(token) } returns null
        coEvery { autDao.getPerfil(any()) } returns Response.success(Usuario(UID = "123", "Erick"))

        repository.traerPerfil(token).toList()

        coVerify(exactly = 1) { autDao.getPerfil(any()) }
        coVerify(exactly = 1) { usuarioDao.añadirUsuario(any()) }
    }
}