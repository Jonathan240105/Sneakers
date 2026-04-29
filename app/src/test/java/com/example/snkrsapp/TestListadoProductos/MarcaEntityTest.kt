package com.example.snkrsapp.TestListadoProductos

import com.example.snkrsapp.Data.LocalData.Marcas.EntityToMarca
import com.example.snkrsapp.Data.LocalData.Marcas.MarcaEntity
import com.example.snkrsapp.Data.LocalData.Marcas.MarcaRespuestaToEntity
import com.example.snkrsapp.Data.RemoteData.ProductoDao.MarcaRespuesta
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate

class MarcaEntityTest {

    //Función que comprueba que se crea un objeto Marca correctamente
    @Test
    fun `Creacion_Objeto_MarcaEntity`() {
        val entity = MarcaEntity(
            idMarca = 10,
            nombre = "Puma",
            paisOrigen = "Alemania",
            fechaFundacion = "fecha",
            logoUrl = "url_puma",
            web = "www.puma.com"
        )

        assertEquals(10, entity.idMarca)
        assertEquals("Puma", entity.nombre)
        assertEquals("fecha", entity.fechaFundacion)
    }

    //Función que comprueba que dos objetos Marca son iguales cuando tienen los mismos atributos
    @Test
    fun `Deberia_mapear_MarcaRespuesta_a_MarcaEntity_correctamente`() {
        val respuestaApi = MarcaRespuesta(
            idMarca = 5,
            nombre = "New Balance",
            paisOrigen = "USA",
            fechaFundacion = "fecha",
            logoUrl = "url",
            web = "web"
        )

        val entityResultante = MarcaRespuestaToEntity(respuestaApi)

        assertEquals(respuestaApi.idMarca, entityResultante.idMarca)
        assertEquals(respuestaApi.nombre, entityResultante.nombre)
        assertEquals(respuestaApi.paisOrigen, entityResultante.paisOrigen)
        assertEquals(respuestaApi.fechaFundacion, entityResultante.fechaFundacion)
        assertEquals(respuestaApi.logoUrl, entityResultante.logoUrl)
        assertEquals(respuestaApi.web, entityResultante.web)
    }

    //Función que comprueba la funcion toMarca funciona correctamente
    @Test
    fun `Convertir_MarcaEntity_a_Marca`() {
        val entity = MarcaEntity(
            7, "Reebok", "reebok_logo"
        )

        val marcaDominio = EntityToMarca(entity)

        assertEquals(entity.idMarca, marcaDominio.idMarca)
        assertEquals(entity.nombre, marcaDominio.nombre)
        assertEquals(entity.logoUrl, marcaDominio.logoUrl)
    }
}