package com.example.snkrsapp.TestListadoProductos

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
        val listaMarcas = listOf("Nike", "Adidas")

        val modelo = ModelPrincipal(
            listaDeproductos = listaProductos,
            exito = true,
            cargando = false,
            listaMarcas = listaMarcas
        )

        assertEquals(listaProductos, modelo.listaDeproductos)
        assertTrue(modelo.exito)
        assertFalse(modelo.cargando)
        assertEquals(listaMarcas, modelo.listaMarcas)
        assertEquals(1, modelo.listaDeproductos.size)
    }

    // Test que comprueba que la función copy,
    // actualiza los campos indicados y deja los demás igual que el copiado
    @Test
    fun Actualizar_Model_Principal() {
        val modeloInicial = ModelPrincipal(
            listaDeproductos = emptyList(),
            exito = false,
            cargando = true,
            listaMarcas = listOf("Jordan", "Nike")
        )

        val productosNuevos = listOf(Producto(idProducto = 2, modelo = "Jordan 1", precio = 200))
        val modeloActualizado = modeloInicial.copy(
            cargando = false,
            exito = true,
            listaDeproductos = productosNuevos
        )

        assertFalse(modeloActualizado.cargando)
        assertTrue(modeloActualizado.exito)
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
            exito = true,
            cargando = false,
            listaMarcas = listOf("Adidas", "Nike")
        )

        val modelo2 = ModelPrincipal(
            listaDeproductos = lista,
            exito = true,
            cargando = false,
            listaMarcas = listOf("Adidas", "Nike")
        )

        assertEquals(modelo1, modelo2)
    }
}