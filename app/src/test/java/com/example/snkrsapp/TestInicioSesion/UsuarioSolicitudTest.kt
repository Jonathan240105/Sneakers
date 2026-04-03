package com.example.snkrsapp.TestInicioSesion

import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuarioEntity
import com.example.snkrsapp.Data.RemoteData.InicioSesion.UsuarioSolicitud
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UsuarioSolicitudTest {

    //Test que comprueba que la creación de un objeto UsuarioEntity
    //se cree correctamente con los campos indicados
    @Test
    fun Creacion_Usuario_Solicitud() {
        val usuario = UsuarioSolicitud(
            "1",
            "Jonathan",
            "hola@gmail.com",
            "Quito",
            "24-01-2005"
        )

        assertEquals("1", usuario.UID)
        assertEquals("Jonathan", usuario.nombreUsuario)
    }

    //Test que comprueba que la función copy,
    //actualiza los campos indicados y deja los demás igual que el copiado
    @Test
    fun Actualizar_Usuario_Solicitud() {
        val usuario1 = UsuarioSolicitud(
            "1",
            "Jonathan",
            "hola@gmail.com",
            "Quito",
            "24-01-2005"
        )

        val usuario2 = usuario1.copy("2")

        assertEquals("2", usuario2.UID)
        assertEquals("Jonathan", usuario2.nombreUsuario)
    }

    //Test que comprueba que crear un objeto UsuarioSolicitud
    //con los mismos valores
    //de los mismos campos es un duplicado
    @Test
    fun Duplicar_Usuario_Solicitud() {
        val usuario1 = UsuarioSolicitud(
            "1",
            "Jonathan",
            "hola@gmail.com",
            "Quito",
            "24-01-2005"
        )
        val usuario2 = UsuarioSolicitud(
            "1",
            "Jonathan",
            "hola@gmail.com",
            "Quito",
            "24-01-2005"
        )

        assertEquals(usuario1, usuario2)
    }
}