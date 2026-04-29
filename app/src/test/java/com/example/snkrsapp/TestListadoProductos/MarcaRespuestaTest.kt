package com.example.snkrsapp.TestListadoProductos

import com.example.snkrsapp.Data.RemoteData.ProductoDao.MarcaRespuesta
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate

class MarcaRespuestaTest {

    // Comprueba que se crea el objeto correctamente con todos sus campos correctamentee
    @Test
    fun `Creacion_Objeto_MarcaRespuesta`() {
        val marca = MarcaRespuesta(
            idMarca = 1,
            nombre = "Nike",
            paisOrigen = "USA",
            fechaFundacion = "fecha",
            logoUrl = "url",
            web = "www.nike.com"
        )

        assertEquals(1, marca.idMarca)
        assertEquals("Nike", marca.nombre)
        assertEquals("USA", marca.paisOrigen)
        assertEquals("fecha", marca.fechaFundacion)
        assertEquals("url", marca.logoUrl)
        assertEquals("www.nike.com", marca.web)
    }

    //Función que comprueba que dos objetos Marca son iguales cuando tienen los mismos atributos
    @Test
    fun `Duplicar_MarcaRespuesta`() {
        val marca1 = MarcaRespuesta(2, "Adidas", "Alemania", "fecha", "url", "web")
        val marca2 = MarcaRespuesta(2, "Adidas", "Alemania", "fecha", "url", "web")

        assertEquals(marca1, marca2)
        assertEquals(marca1.hashCode(), marca2.hashCode())
    }

    //Función que comprueba que al actualizar un atributo de una Marca se actualiza correctamente
    @Test
    fun `Actualizar_atributo_de_MarcaRespuesta`() {
        val marcaOriginal = MarcaRespuesta(3, "MarcaX", "España", "fecha", "url", "web")

        val marcaActualizada = marcaOriginal.copy(
            nombre = "MarcaY",
            fechaFundacion = "nuevaFecha"
        )

        assertEquals("MarcaY", marcaActualizada.nombre)
        assertEquals("nuevaFecha", marcaActualizada.fechaFundacion)

        assertEquals(3, marcaActualizada.idMarca)
        assertEquals("España", marcaActualizada.paisOrigen)
        assertEquals("url", marcaActualizada.logoUrl)
    }

}