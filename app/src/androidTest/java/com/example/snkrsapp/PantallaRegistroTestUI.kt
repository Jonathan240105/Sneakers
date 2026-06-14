package com.example.snkrsapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.snkrsapp.Domain.ModelRegistro
import com.example.snkrsapp.Views.Pantallas.PantallaRegistro
import com.example.snkrsapp.Views.ViewModels.RegistroViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PantallaRegistroTestUI {

    @get:Rule
    val composeRule = createComposeRule()

    fun lanzarPantallaRegistro() {
        val viewModel = mockk<RegistroViewModel>()
        val estadoInicial = MutableStateFlow(
            ModelRegistro(
                exito = false,
                cargando = false,
                errorFirebase = false
            )
        )
        composeRule.setContent {
            every {
                viewModel.model
            } returns estadoInicial.asStateFlow()
            PantallaRegistro(viewModel)
        }
    }

    //función que comprueba que el titulo del registro existe en la pantalla y que muestra lo esperado
    @Test
    fun Comprobar_Titulo_Existe() {
        lanzarPantallaRegistro()
        composeRule.onNodeWithTag("tituloRegistro").assertExists()
        composeRule.onNodeWithTag("tituloRegistro").assertTextEquals("¡Unete a nosotros!")
    }

    //Función que comprueba que el flujo del registro es correcto, es decir que muestra lo esperado en cada paso
    @Test
    fun Comprobar_Flujo_Registro_Completo() {
        lanzarPantallaRegistro()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("textFieldEmail").assertExists()
        composeRule.onNodeWithTag("textFieldEmail").performTextInput("test@gmail.com")

        composeRule.onNodeWithTag("textFieldContra").assertExists()
        composeRule.onNodeWithTag("textFieldContra").performTextInput("1234567")

        composeRule.onNodeWithTag("botonPrimerPaso").assertExists()
        composeRule.onNodeWithTag("botonPrimerPaso").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("textFieldNombreUsuario").assertExists()
        composeRule.onNodeWithTag("textFieldNombreUsuario").performTextInput("pepe123")

        composeRule.onNodeWithTag("textFieldApellidos").assertExists()
        composeRule.onNodeWithTag("textFieldApellidos")
            .performTextInput("apellido1 apellidodnewajd")

        composeRule.onNodeWithTag("botonSegundoPaso").assertExists()
        composeRule.onNodeWithTag("botonSegundoPaso").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("SelectorFecha").assertExists()
    }

    //Función que comprueba que el botón de volver funciona correctamente
    @Test
    fun Comprobar_Boton_Volver_Funciona() {
        lanzarPantallaRegistro()

        composeRule.onNodeWithTag("botonPrimerPaso").performClick()

        composeRule.onNodeWithTag("botonVolver1").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("textFieldEmail").assertIsDisplayed()
    }

}
