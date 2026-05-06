package com.example.snkrsapp.TestPerfil

import com.example.snkrsapp.Data.RemoteData.ActualizacionDao.ActualizarPerfilRespuesta
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ActualizarPerfilRespuestaTest {

    //Función que comprueba que la creación de un objeto ActualizarPerfilRespuesta se cree correctamente con los campos indicados
    @Test
    fun Creacion_ActualizarPerfilRespuesta() {
        val respuesta = ActualizarPerfilRespuesta(true, "Datos actualizados")

        assertTrue(respuesta.ok)
        assertEquals("Datos actualizados", respuesta.message)
    }

    //Función que comprueba que la actualización de un objeto ActualizarPerfilRespuesta se haga correctamente con los campos indicados
    @Test
    fun Actualizar_ActualizarPerfilRespuesta() {
        val respuestaExitosa = ActualizarPerfilRespuesta(true, "OK")

        val respuestaError = respuestaExitosa.copy(false, "Error")

        assertFalse(respuestaError.ok)
        assertEquals("Error", respuestaError.message)
    }

    //Función que comprueba que crear un objeto ActualizarPerfilRespuesta con los mismos valores de los mismos campos es un duplicado
    @Test
    fun Duplicar_ActualizarPerfilRespuesta() {
        val res1 = ActualizarPerfilRespuesta(true, "Mensaje")
        val res2 = ActualizarPerfilRespuesta(true, "Mensaje")

        assertEquals(res1, res2)
    }
}