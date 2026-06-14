package com.example.snkrsapp.Views.ViewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.snkrsapp.Data.Repository.UsuarioRepository.UsuarioRepository
import com.example.snkrsapp.Dispatcher
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class PerfilViewModelTest {

    private val repository = mockk<UsuarioRepository>()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcher = Dispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `cuando cargarPerfil es llamado, el modelo se actualiza con el usuario del repositorio`() =
        runTest {

            val viewModel = PerfilViewModel(repository)
            val usuarioDummy = Usuario(
                UID = "abc",
                nombreUsuario = "Erick",
                email = "erick@gmail.com",
                apellidos = "S",
                fechaNacimiento = "2005-01-24"
            )

            coEvery { repository.traerPerfil(any()) } returns flowOf(usuarioDummy)

            viewModel.cargarPerfil()
            advanceUntilIdle()

            val estadoActual = viewModel.model.value
            assert(estadoActual.usuarioActual == usuarioDummy)
            assert(estadoActual.exito)
            assert(!estadoActual.cargando)
        }
}