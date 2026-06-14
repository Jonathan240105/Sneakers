package com.example.snkrsapp.TestListadoProductos

import com.example.snkrsapp.Domain.Marca
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MarcaTest {

    //Función que comprueba que se crea un objeto Marca correctamente
    @Test
    fun `Creacion_Objeto_Marca`() {

        val marca = Marca(1, "Nike", "url")

        assertEquals(1, marca.idMarca)
        assertEquals("Nike", marca.nombre)
        assertEquals("url", marca.logoUrl)
    }

    //Función que comprueba que dos objetos Marca son iguales cuando tienen los mismos atributos
    @Test
    fun `Duplicar_marca`() {
        val marca1 = Marca(2, "Adidas", "url")
        val marca2 = Marca(2, "Adidas", "url")

        assertEquals(marca1, marca2)
        assertEquals(marca1.hashCode(), marca2.hashCode())
    }

    //Función que comprueba que al actualizar un atributo de una marca se actualiza correctamente
    @Test
    fun `Actualizar_atributo_de_marca`() {
        val marcaOriginal = Marca(3, "Tesla", "url")

        val marcaActualizada = marcaOriginal.copy(nombre = "Apple")

        assertEquals(3, marcaActualizada.idMarca)
        assertEquals("Apple", marcaActualizada.nombre)
        assertEquals("url", marcaActualizada.logoUrl)
    }
}