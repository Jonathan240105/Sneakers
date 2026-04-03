package com.example.snkrsapp.TestInicioSesion

import com.example.snkrsapp.Data.RemoteData.InicioSesion.InicioSesionRespuesta
import com.example.snkrsapp.Data.RemoteData.InicioSesion.Usuario
import junit.framework.TestCase.assertEquals
import org.junit.Test

class InicioSesionRespuestaTest {

    //Test que comprueba que la creación de un objeto InicioSesionRespuesta
    //se cree correctamente con los campos indicados
    @Test
    fun Creacion_InicioSesionRespuesta() {
        val respuesta = InicioSesionRespuesta("hola", Usuario("1"))

        assertEquals("hola", respuesta.message)
        assertEquals("1", respuesta.usuario?.UID)

    }

    //Test que comprueba que la función copy,
    //actualiza los campos indicados y deja los demás igual que el copiado
    @Test
    fun Actualizar_InicioSesionRespuesta() {
        val respuesta = InicioSesionRespuesta("hola", Usuario("1"))

        val respuesta2 = respuesta.copy("adios")

        assertEquals("adios", respuesta2.message)
        assertEquals("1", respuesta2.usuario?.UID)
    }

    //Test que comprueba que crear un objeto con los mismos valores
    //de los mismos campos es un duplicado
    @Test
    fun Duplicar_InicioSesionRespuesta() {
        val respuesta = InicioSesionRespuesta("hola", Usuario("1"))
        val respuesta2 = InicioSesionRespuesta("hola", Usuario("1"))

        assertEquals(respuesta, respuesta2)

    }
}