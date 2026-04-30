package com.example.snkrsapp.TestProductoDetallado

import com.example.snkrsapp.Data.LocalData.Marcas.MarcaEntity
import com.example.snkrsapp.Data.LocalData.Marcas.MarcaLocalDao
import com.example.snkrsapp.Data.LocalData.Productos.ProductoEntity
import com.example.snkrsapp.Data.LocalData.Productos.ProductoLocalDao
import com.example.snkrsapp.Data.RemoteData.ProductoDao.ProductosDao
import com.example.snkrsapp.Data.Repository.ProductoRepository.ProductoRepositoryImp
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ProductoDetalladoRepositoryTest {

    private val productosDao = mockk<ProductosDao>(relaxed = true)
    private val productoLocalDao = mockk<ProductoLocalDao>(relaxed = true)
    private val marcaLocalDao = mockk<MarcaLocalDao>(relaxed = true)
    private lateinit var repository: ProductoRepositoryImp

    @Before
    fun setup() {
        repository = ProductoRepositoryImp(productosDao, productoLocalDao, marcaLocalDao)
    }

    @Test
    fun `comprobar_que_al_llamar_a_la_funcion_se_trae_el_objeto_correctamente`() = runTest {
        val productoId = 10
        val productoLocal =
            ProductoEntity(idProducto = productoId, modelo = "Jordan 1", precio = 150)

        coEvery { productoLocalDao.obtenerProductoPorId(productoId) } returns productoLocal

        repository.traerProductoPorId(productoId).collect {
            assertEquals(productoLocal.idProducto, it.idProducto)
        }
        coVerify(exactly = 1) { productoLocalDao.obtenerProductoPorId(productoId) }
    }

    @Test
    fun `traerMarcaPorId_emite_el_objeto_si_existe_en_bd`() = runTest {
        val marcaId = 5
        val marcaLocal = MarcaEntity(
            idMarca = marcaId,
            nombre = "Jordan",
            paisOrigen = "USA",
            fechaFundacion = "1984",
            logoUrl = "",
            web = ""
        )

        coEvery { marcaLocalDao.obtenerMarcaPorId(marcaId) } returns marcaLocal

        repository.traerMarcaPorId(marcaId).collect {
            assertEquals(marcaLocal.idMarca, it.idMarca)
        }
        coVerify(exactly = 1) { marcaLocalDao.obtenerMarcaPorId(marcaId) }
    }
}