package com.example.snkrsapp.TestInicioSesion

import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario
import com.example.snkrsapp.Domain.EstadoLogin
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class EstadoLoginTest {


    @Test
    fun Cuando_el_estado_es_Exito_debe_contener_el_usuario_correcto() {
        val usuario = Usuario("1")
        val estado = EstadoLogin.Exito(usuario)

        assertEquals("1", estado.usario.UID)
    }

    @Test
    fun Creacion_Error_Normal() {
        val mensajeError = "la contraseña no coincide"

        val estado = EstadoLogin.Error(mensajeError, false)

        assertEquals("la contraseña no coincide", estado.mensaje)
        assertFalse(estado.errorFirebase)
    }

    @Test
    fun Creacion_EstadoLogin_Error_firebase() {
        val mensajeError = "Email ya registrado"

        val estado = EstadoLogin.Error(mensajeError, true)

        assertEquals(mensajeError, estado.mensaje)
        assertTrue(estado.errorFirebase)
    }

    @Test
    fun Cargando_debe_ser_del_tipo_EstadoRegistro() {
        val estado: Any = EstadoLogin.Cargando

        assertTrue(estado is EstadoLogin)
    }
}