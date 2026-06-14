package com.example.snkrsapp.TestListadoProductos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.snkrsapp.Data.Repository.ProductoRepository.ProductoRepository
import com.example.snkrsapp.Dispatcher
import com.example.snkrsapp.Domain.Marca
import com.example.snkrsapp.Domain.Producto
import com.example.snkrsapp.Views.ViewModels.PrincipalViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class PrincipalViewModelTest {
    private val repository = mockk<ProductoRepository>()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcher = Dispatcher()

    @Test
    fun `cuando listarProductos es llamado, el modelo se actualiza con la lista del repositorio`() =
        runTest {
            val listaFalsa = listOf(
                Producto(idProducto = 1, modelo = "Jordan 1", precio = 150, imagenUrl = ""),
                Producto(idProducto = 2, modelo = "Dunk Low", precio = 110, imagenUrl = "")
            )
            coEvery { repository.traerPaginaProductos(any(), any()) } returns flowOf(listaFalsa)

            val viewmodel = PrincipalViewModel(repository)

            val estadoActual = viewmodel.model.value

            assertEquals(listaFalsa, estadoActual.listaDeproductos)
            assertTrue(estadoActual.exitoProductos)
            assertEquals(false, estadoActual.cargandoProductos)
        }

    @Test
    fun `cuando el repositorio falla, el modelo refleja error`() = runTest {
        coEvery { repository.traerPaginaProductos(any(), any()) } throws Exception("Error de red")

        val viewmodel = PrincipalViewModel(repository)
        viewmodel.cargarPaginaProductos()

        val estadoActual = viewmodel.model.value
        assertEquals(false, estadoActual.exitoProductos)
        assertEquals(false, estadoActual.cargandoProductos)
    }

    @Test
    fun `cuando cargarMarcas es llamado, el modelo se actualiza con las marcas del repositorio`() =
        runTest {
            val marcasFalsas = listOf(
                Marca(1, "Nike", "USA"),
                Marca(2, "Adidas", "Alemania")
            )
            coEvery { repository.traerMarcas() } returns flowOf(marcasFalsas)

            coEvery { repository.traerPaginaProductos(any(), any()) } returns flowOf(emptyList())

            val viewmodel = PrincipalViewModel(repository)


            val estadoActual = viewmodel.model.value
            assertEquals(marcasFalsas, estadoActual.listaMarcas)
            assertTrue(estadoActual.exitoMarcas)
            assertEquals(false, estadoActual.cargandoMarcas)
        }

    @Test
    fun `cuando el repositorio de marcas falla, el modelo refleja error`() = runTest {
        // GIVEN: El repositorio lanza una excepción
        coEvery { repository.traerMarcas() } throws Exception("Error al cargar marcas")
        coEvery { repository.traerPaginaProductos(any(), any()) } returns flowOf(emptyList())

        val viewmodel = PrincipalViewModel(repository)

        // WHEN
        viewmodel.cargarMarcas()

        // THEN
        val estadoActual = viewmodel.model.value
        assertEquals(false, estadoActual.exitoMarcas)
        assertEquals(false, estadoActual.cargandoMarcas)
    }
}