package com.example.snkrsapp.TestListadoProductos

import com.example.snkrsapp.Domain.Marca
import com.example.snkrsapp.Domain.ModelPrincipal
import com.example.snkrsapp.Domain.Producto
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ModelPrincipalTest {

    //Función que comprueba que la creación de un objeto ModelPrincipal se cree correctamente con los campos indicados
    @Test
    fun Creacion_Model_Principal() {
        val listaProductos = listOf(
            Producto(1, modelo = "Test Sneaker", precio = 100)
        )
        val listaMarcas = listOf(
            Marca(1, "marca", ""),
            Marca(1, "marca", "")
        )

        val modelo = ModelPrincipal(
            listaDeproductos = listaProductos,
            exitoProductos = true,
            exitoMarcas = true,
            cargandoProductos = false,
            cargandoMarcas = true,
            listaMarcas = listaMarcas
        )

        assertEquals(listaProductos, modelo.listaDeproductos)
        assertTrue(modelo.exitoProductos)
        assertFalse(modelo.cargandoProductos)
        assertEquals(listaMarcas, modelo.listaMarcas)
        assertEquals(1, modelo.listaDeproductos.size)
    }

    // Test que comprueba que la función copy,
    // actualiza los campos indicados y deja los demás igual que el copiado
    @Test
    fun Actualizar_Model_Principal() {
        val modeloInicial = ModelPrincipal(
            listaDeproductos = emptyList(),
            exitoProductos = false,
            exitoMarcas = false,
            cargandoProductos = true,
            cargandoMarcas = true,
            listaMarcas = listOf(
                Marca(1, "marca", "url"),
                Marca(1, "marca", "url")
            )
        )

        val productosNuevos = listOf(Producto(idProducto = 2, modelo = "Jordan 1", precio = 200))
        val modeloActualizado = modeloInicial.copy(
            cargandoProductos = false,
            exitoProductos = true,
            listaDeproductos = productosNuevos
        )

        assertFalse(modeloActualizado.cargandoProductos)
        assertTrue(modeloActualizado.exitoProductos)
        assertEquals(productosNuevos, modeloActualizado.listaDeproductos)
        assertEquals(modeloInicial.listaMarcas, modeloActualizado.listaMarcas)
    }

    // Test que comprueba que crear un objeto ModelPrincipal
    // con los mismos valores de los mismos campos es un duplicado
    @Test
    fun Duplicar_Model_Principal() {
        val lista = listOf(Producto(idProducto = 3, modelo = "Yeezy", precio = 300))

        val modelo1 = ModelPrincipal(
            listaDeproductos = lista,
            exitoProductos = true,
            exitoMarcas = true,
            cargandoProductos = false,
            cargandoMarcas = true,
            listaMarcas = listOf(
                Marca(1, "marca", "url"),
                Marca(1, "marca", "url")
            )
        )

        val modelo2 = ModelPrincipal(
            listaDeproductos = lista,
            exitoProductos = true,
            exitoMarcas = true,
            cargandoProductos = false,
            cargandoMarcas = true,
            listaMarcas = listOf(
                Marca(1, "marca", "url"),
                Marca(1, "marca", "url")
            )
        )

        assertEquals(modelo1, modelo2)
    }
}