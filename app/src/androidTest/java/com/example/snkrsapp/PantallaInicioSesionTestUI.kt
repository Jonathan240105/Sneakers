package com.example.snkrsapp

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.snkrsapp.Domain.ModelInicioSesion
import com.example.snkrsapp.Views.Pantallas.PantallaInicioSesion
import com.example.snkrsapp.Views.ViewModels.InicioSesionViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PantallaInicioSesionTestUI {

    @get:Rule
    val composeRule = createComposeRule()


    //Función para lanzar la pantalla de inicio de sesión
    fun lanzarPantallaInicioSesion() {
        val viewmodel = mockk<InicioSesionViewModel>()
        val estadoInicial = MutableStateFlow(
            ModelInicioSesion(
                exito = false,
                cargando = false,
                errorFirebase = false
            )
        )
        composeRule.setContent {
            every { viewmodel.model } returns estadoInicial.asStateFlow()
            PantallaInicioSesion(
                viewmodel, {}, {}
            )
        }
    }

    //Función que comprueba que el título existe en la pantalla, y que muestra lo esperado
    @Test
    fun Comprobar_titulo_existe() {
        lanzarPantallaInicioSesion()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("tituloInicioSesion").assertExists()
        composeRule.onNodeWithTag("tituloInicioSesion").assertTextEquals("Bienvenido!")
    }

    //Función que comprueba que el textfield de email existe en la pantalla, y que muestra lo que el usuario escribe
    @Test
    fun Comprobar_TextField_Email() {
        lanzarPantallaInicioSesion()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("textFieldEmail").assertExists()
        composeRule.onNodeWithTag("textFieldEmail").performTextInput("email123")
        composeRule.onNodeWithTag("textFieldEmail").assert(hasText("email123"))
    }

    //Función que comprueba que el textfield de contraseñ existe en la pantalla, y que muestra lo que el usuario escribe
    @Test
    fun Comprobar_TextField_Contra() {
        lanzarPantallaInicioSesion()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("textFieldContra").assertExists()
        composeRule.onNodeWithTag("textFieldContra").performTextInput("contra123")
        composeRule.onNodeWithTag("textFieldContra").assert(hasText("contra123"))
    }

    @Test
    fun Comprobar_Boton_InicioSesion() {
        lanzarPantallaInicioSesion()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("botonInicioSesion").assertExists()
        composeRule.onNodeWithTag("botonInicioSesion").assertTextEquals("Iniciar sesión")

    }
}