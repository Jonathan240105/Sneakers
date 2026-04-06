package com.example.snkrsapp.TestInicioSesion

import com.example.snkrsapp.Domain.ModelInicioSesion
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ModelInicioSesionTest {

    //Test que comprueba que la creación de un objeto ModelInicioSesion
    //se cree correctamente con los campos indicados
    @Test
    fun Creacion_ModelInicioSesion() {

        val model = ModelInicioSesion(false, false)

        assertFalse(model.exito)
        assertFalse(model.cargando)
    }

    //Test que comprueba que la función copy,
    //actualiza los campos indicados y deja los demás igual que el copiado
    @Test
    fun Actualizar_ModelInicioSesion() {
        val model = ModelInicioSesion(false, false)

        val model2 = model.copy(true)

        assertFalse(model.cargando)
        assertTrue(model2.exito)

    }

    //Test que comprueba que crear un objeto ModelInicioSesion con los mismos valores
    //de los mismos campos es un duplicado
    @Test
    fun Duplicar_ModelInicioSesion() {
        val model = ModelInicioSesion(false, false)
        val model2 = ModelInicioSesion(false, false)

        assertEquals(model, model2)

    }
}