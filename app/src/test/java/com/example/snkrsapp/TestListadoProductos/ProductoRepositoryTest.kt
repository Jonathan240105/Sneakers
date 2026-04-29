package com.example.snkrsapp.TestListadoProductos

import com.example.snkrsapp.Data.LocalData.Marcas.MarcaLocalDao
import com.example.snkrsapp.Data.LocalData.Productos.ProductoEntity
import com.example.snkrsapp.Data.LocalData.Productos.ProductoLocalDao
import com.example.snkrsapp.Data.RemoteData.ProductoDao.ProductosDao
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
    private val productosDao = mockk<ProductosDao>(relaxed = true)
    private val productoLocalDao = mockk<ProductoLocalDao>(relaxed = true)
    private val marcasDao = mockk<MarcaLocalDao>(relaxed = true)
    private lateinit var repository: ProductoRepositoryImp

    @Before
    fun setup() {
        repository = ProductoRepositoryImp(productosDao, productoLocalDao,marcasDao)
    }

    @Test
    fun `si_la_bd_tiene_datos_no_se_llama_a_la_api`() = runTest {
        val listaLocal = listOf(ProductoEntity(1, modelo = "Modelo1", precio = 100))
        coEvery { productoLocalDao.obtenerPaginaProductos(any(), any()) } returns listaLocal

        repository.traerPaginaProductos(0, 20).collect()

        coVerify(exactly = 1) { productoLocalDao.obtenerPaginaProductos(any(), any()) }
        coVerify(exactly = 0) { productosDao.obtenerPaginaProductos(any(), any()) }
    }

    @Test
    fun `si_la_bd_esta_vacía_se_llama_a_la_api`() = runTest {
        coEvery { productoLocalDao.obtenerPaginaProductos(any(), any()) } returns emptyList()

        coEvery { productosDao.obtenerPaginaProductos(any(), any()) } returns Response.success(
            emptyList()
        )

        repository.traerPaginaProductos(0, 20).collect()

        coVerify(exactly = 1) { productosDao.obtenerPaginaProductos(any(), any()) }
    }
    @Test
    fun `si_la_bd_tiene_marcas_no_se_llama_a_la_api`() = runTest {
        val marcasLocales = listOf(
            com.example.snkrsapp.Data.LocalData.Marcas.MarcaEntity(1, "Nike", "USA", "1964", "url", "web")
        )
        coEvery { marcasDao.obtenerMarcas() } returns marcasLocales

        repository.traerMarcas().collect()

        coVerify(exactly = 1) { marcasDao.obtenerMarcas() }
        coVerify(exactly = 0) { productosDao.obtenerMarcas() }
    }

    @Test
    fun `si_la_bd_no_tiene_marcas_se_llama_a_la_api_e_inserta_en_local`() = runTest {
        coEvery { marcasDao.obtenerMarcas() } returns emptyList()
        coEvery { productosDao.obtenerMarcas() } returns Response.success(emptyList())

        repository.traerMarcas().collect()

        coVerify(exactly = 1) { productosDao.obtenerMarcas() }
        coVerify { marcasDao.insertarListaMarcas(any()) }
    }
}