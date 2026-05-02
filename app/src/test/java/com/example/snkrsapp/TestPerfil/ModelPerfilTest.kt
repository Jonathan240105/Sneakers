package com.example.snkrsapp.TestPerfil

import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario
import com.example.snkrsapp.Domain.ModelPerfil
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ModelPerfilTest {

    //Función que comprueba que la creación de un objeto ModelPerfil se cree correctamente con los campos indicados
    @Test
    fun Creacion_Model_Perfil() {

        val usuario = Usuario(
            "1",
            "jonathan",
            "jonathan@gmail.com",
            "apellidos",
            "01-01-2005"
        )
        val modelo = ModelPerfil(
            usuario,
            true,
            false
        )

        assertEquals(usuario, modelo.usuarioActual)
        assertTrue(modelo.exito)
        assertFalse(modelo.cargando)
    }

    //Función que comprueba que la actualización de un objeto ModelPerfil se haga correctamente con los campos indicados
    @Test
    fun Actualizar_Model_Perfil() {
        val usuario = Usuario(
            "1",
            "jonathan",
            "jonathan@gmail.com",
            "apellidos",
            "01-01-2005"
        )
        val modelo = ModelPerfil(
            usuario,
            true,
            false
        )

        val nuevoModelo = modelo.copy(
            usuarioActual = usuario,
            exito = false,
            cargando = true
        )

        assertEquals(usuario, nuevoModelo.usuarioActual)    
        assertFalse(nuevoModelo.exito)
        assertTrue(nuevoModelo.cargando)
    }

    //Función que comprueba que crear un objeto ModelPerfil con los mismos valores
    // de los mismos campos es un duplicado
    @Test
    fun Duplicar_Model_Perfil() {
        val usuario = Usuario(
            "1",
            "jonathan",
            "jonathan@gmail.com",
            "apellidos",
            "01-01-2005"
        )
        val modelo1 = ModelPerfil(
            usuario,
            true,
            false
        )
        val modelo2 = ModelPerfil(
            usuario,
            true,
            false
        )
        assertEquals(modelo1, modelo2)
    }
}