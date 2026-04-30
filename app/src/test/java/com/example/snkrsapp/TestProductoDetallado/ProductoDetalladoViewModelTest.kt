package com.example.snkrsapp.TestProductoDetallado

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.snkrsapp.Data.Repository.ProductoRepository.ProductoRepository
import com.example.snkrsapp.Dispatcher
import com.example.snkrsapp.Domain.Marca
import com.example.snkrsapp.Domain.Producto
import com.example.snkrsapp.Views.ViewModels.ProductoDetalladoViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class ProductoDetalladoViewModelTest {
    private val repository = mockk<ProductoRepository>()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcher = Dispatcher()

    @Test
    fun `cuando cargarProducto es llamado, el modelo se actualiza con el producto del repositorio`() =
        runTest {
            val viewmodel = ProductoDetalladoViewModel(repository)

            val producto = Producto(1, 1, "Jordan 2", 150)
            coEvery { repository.traerProductoPorId(any()) } returns flowOf(producto)
            viewmodel.cargarProducto(1)
            val estadoActual = viewmodel.model.value
            assert(estadoActual.productoSeleccionado == producto)

        }

    @Test
    fun `cuando cargarMarca es llamado, el modelo se actualiza con la marca del repositorio`() =
        runTest {

            val viewmodel = ProductoDetalladoViewModel(repository)

            val marca = Marca(1, "Nike", "USA")

            coEvery { repository.traerMarcaPorId(any()) } returns flowOf(marca)

            viewmodel.cargarMarca(1)
            val estadoActual = viewmodel.model.value
            assert(estadoActual.marcaSeleccionada == marca)
        }
}