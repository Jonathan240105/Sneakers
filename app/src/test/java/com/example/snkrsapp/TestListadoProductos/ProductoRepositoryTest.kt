package com.example.snkrsapp.TestListadoProductos

import com.example.snkrsapp.Data.LocalData.Productos.ProductoEntity
import com.example.snkrsapp.Data.LocalData.Productos.ProductoLocalDao
import com.example.snkrsapp.Data.RemoteData.ProductoDao.ProductoDao
import com.example.snkrsapp.Data.Repository.ProductoRepository.ProductoRepositoryImp
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ProductoRepositoryTest {
    private val productoDao = mockk<ProductoDao>(relaxed = true)
    private val productoLocalDao = mockk<ProductoLocalDao>(relaxed = true)
    private lateinit var repository: ProductoRepositoryImp

    @Before
    fun setup() {
        repository = ProductoRepositoryImp(productoDao, productoLocalDao)
    }

    @Test
    fun `si_la_bd_tiene_datos_no_se_llama_a_la_api`() = runTest {
        val listaLocal = listOf(ProductoEntity(1, modelo = "Modelo1", precio = 100))
        coEvery { productoLocalDao.obtenerProductos() } returns listaLocal

        repository.obtenerProductos().collect()

        coVerify(exactly = 1) { productoLocalDao.obtenerProductos() }
        coVerify(exactly = 0) { productoDao.obtenerProductos() }
    }

    @Test
    fun `si_la_bd_esta_vacía_se_llama_a_la_api`() = runTest {
        coEvery { productoLocalDao.obtenerProductos() } returns emptyList()

        coEvery { productoDao.obtenerProductos() } returns Response.success(emptyList())

        repository.obtenerProductos().collect()

        coVerify(exactly = 1) { productoDao.obtenerProductos() }
    }
}