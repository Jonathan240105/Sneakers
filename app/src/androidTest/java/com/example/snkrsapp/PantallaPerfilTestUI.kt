package com.example.snkrsapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario
import com.example.snkrsapp.Domain.ModelPerfil
import com.example.snkrsapp.Views.Pantallas.PantallaPerfil
import com.example.snkrsapp.Views.ViewModels.PerfilViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PantallaPerfilTestUI {

    @get:Rule
    val composeRule = createComposeRule()

    //Función para lanzar la pantalla de perfil
    fun lanzarPantallaPerfil(cambiarAConfig: () -> Unit = {}) {
        val viewModel = mockk<PerfilViewModel>(relaxed = true)
        val usuario = Usuario("1", "Jonathan", "email", "apellidos", "2005-01-01")
        val estadoInicial = MutableStateFlow(
            ModelPerfil(usuario, false, false)
        )

        every { viewModel.model } returns estadoInicial
        composeRule.setContent {
            PantallaPerfil(cambiarAConfig, viewModel)
        }
    }

    @Test
    fun Comprobar_datos_se_muestran_correctamente() {
        lanzarPantallaPerfil()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("@Jonathan", substring = true).assertIsDisplayed()
        composeRule.onNodeWithTag("HeaderPerfil").assertIsDisplayed()
        composeRule.onNodeWithText("Mi Colección").assertExists()
        composeRule.onNodeWithText("Mis Ventas").assertExists()
    }

    @Test
    fun Comprobar_boton_config_abre_pantalla_config() {
        var mostrarConfig = false

        lanzarPantallaPerfil(cambiarAConfig = { mostrarConfig = true })

        composeRule.onNodeWithTag("BotonConfig").performClick()

        assert(mostrarConfig)
    }
}