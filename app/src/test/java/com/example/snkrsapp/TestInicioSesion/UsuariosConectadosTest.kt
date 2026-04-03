package com.example.snkrsapp.TestInicioSesion

import com.example.snkrsapp.Data.LocalData.UsuariosConectados.EntityToUsuario
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuarioEntity
import com.example.snkrsapp.Data.LocalData.UsuariosConectados.UsuarioToEntity
import com.example.snkrsapp.Data.RemoteData.InicioSesion.Usuario
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class UsuariosConectadosTest {

    //Test que comprueba que la creación de un objeto UsuarioEntity
    //se cree correctamente con los campos indicados
    @Test
    fun Creacion_Usuario_Entity() {
        val usuario1 = UsuarioEntity(
            "1",
            "Jonathan",
            "hola@gmail.com",
            "Quito",
            "24-01-2005"
        )

        assertEquals("1", usuario1.UID)
        assertEquals("Jonathan", usuario1.nombreUsuario)
    }

    //Test para comprobar que la función copy,
    //actualiza los campos indicados y deja los demás igual que el copiado
    @Test
    fun Actualizar_Usuario_Entity() {
        val usuario1 = UsuarioEntity(
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

    //Test que comprueba que crear un objeto con los mismos valores
    //de los mismos campos es un duplicado
    @Test
    fun Duplicar_Usuario_Entity() {
        val usuario1 = UsuarioEntity(
            "1",
            "Jonathan",
            "hola@gmail.com",
            "Quito",
            "24-01-2005"
        )
        val usuario2 = UsuarioEntity(
            "1",
            "Jonathan",
            "hola@gmail.com",
            "Quito",
            "24-01-2005"
        )

        assertEquals(usuario1, usuario2)
    }

    //Test que comprueba que la función UsuarioToEntity,
    //convierte un objeto Usuario en un objeto UsuarioEntity
    @Test
    fun Convertir_Usuario_Entity_a_Usuario() {
        val usuario = Usuario(
            "1",
            "Jonathan",
            "hola@gmail.com",
            "Quito",
            "24-01-2005"
        )

        val entity = UsuarioToEntity(usuario)

        assertTrue(entity is UsuarioEntity)
    }

    //test que comprueba que la función EntityToUsuario,
    //convierte un objeto UsuarioEntity en un objeto Usuario
    @Test
    fun Convertir_UsuarioEntity_a_Usuario() {
        val entity = UsuarioEntity(
            "1",
            "Jonathan",
            "hola@gmail.com",
            "Quito",
            "24-01-2005"
        )

        val usuario = EntityToUsuario(entity)

        assertTrue(usuario is Usuario)
    }
}