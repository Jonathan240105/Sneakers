package com.example.snkrsapp.TestPerfil

import com.example.snkrsapp.Data.RemoteData.ActualizacionDao.ActualizarPerfilSolicitud
import org.junit.Test
import org.junit.Assert.*

class ActualizarPerfilSolicitudTest {

    //Función que comprueba que la creación de un objeto ActualizarPerfilSolicitud se cree correctamente con los campos indicados
    @Test
    fun Creacion_ActualizarPerfilSolicitud() {
        val solicitud = ActualizarPerfilSolicitud(
            nombreUsuario = "Jonathan",
            apellidos = "apellidos",
            password = "123"
        )

        assertEquals("Jonathan", solicitud.nombreUsuario)
        assertEquals("apellidos", solicitud.apellidos)
        assertEquals("123", solicitud.password)
    }


    //Función que comprueba que la actualización de un objeto ActualizarPerfilSolicitud se haga correctamente con los campos indicados
    @Test
    fun Actualizar_ActualizarPerfilSolicitud() {
        val original = ActualizarPerfilSolicitud(nombreUsuario = "Original")

        val actualizado = original.copy(apellidos = "Nuevo Apellido")

        assertEquals("Original", actualizado.nombreUsuario)
        assertEquals("Nuevo Apellido", actualizado.apellidos)
    }

    //Función que comprueba que crear un objeto ActualizarPerfilSolicitud con los mismos valores de los mismos campos es un duplicado
    @Test
    fun Duplicado_ActualizarPerfilSolicitud() {
        val solicitud1 = ActualizarPerfilSolicitud("User", "Apellido", "123")
        val solicitud2 = ActualizarPerfilSolicitud("User", "Apellido", "123")

        assertEquals(solicitud1, solicitud2)
        assertEquals(solicitud1.hashCode(), solicitud2.hashCode())
    }
}