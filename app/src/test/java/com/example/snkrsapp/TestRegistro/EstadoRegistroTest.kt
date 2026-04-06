package com.example.snkrsapp.TestRegistro

import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario
import com.example.snkrsapp.Domain.EstadoRegistro
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class EstadoRegistroTest {

    @Test
    fun Cuando_el_estado_es_Exito_debe_contener_el_mensaje_correcto() {
        val mensajeExito = "Todo bien"
        val estado = EstadoRegistro.Exito(mensajeExito)

        assertEquals("Todo bien", estado.mensaje)
    }

    @Test
    fun Creacion_Error_Normal() {
        val mensajeError = "la contraseña es muy corta"

        val estado = EstadoRegistro.Error(mensajeError, false)

        assertEquals(mensajeError, estado.mensaje)
        assertFalse(estado.errorFirebase)
    }

    @Test
    fun Creacion_EstadoLogin_Error_firebase() {
        val mensajeError = "Email ya registrado"

        val estado = EstadoRegistro.Error(mensajeError, true)

        assertEquals(mensajeError, estado.mensaje)
        assertTrue(estado.errorFirebase)
    }

    @Test
    fun Cargando_debe_ser_del_tipo_EstadoRegistro() {
        val estado: Any = EstadoRegistro.Cargando

        assertTrue(estado is EstadoRegistro)
    }
}