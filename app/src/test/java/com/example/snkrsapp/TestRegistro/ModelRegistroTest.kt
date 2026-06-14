package com.example.snkrsapp.TestRegistro

import com.example.snkrsapp.Domain.ModelRegistro
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ModelRegistroTest {

    @Test
    fun Creacion_ModelRegistro() {
        val model = ModelRegistro(false, false, false)

        assertFalse(model.exito)
        assertFalse(model.cargando)
        assertFalse(model.errorFirebase)
    }

    @Test
    fun Actualizar_ModelRegistro() {
        val model = ModelRegistro(false, false)

        val model2 = model.copy(true)

        assertFalse(model.cargando)
        assertTrue(model2.exito)

    }

    @Test
    fun Duplicar_ModelRegistro() {
        val model = ModelRegistro(false, false)
        val model2 = ModelRegistro(false, false)

        assertEquals(model, model2)

    }
}