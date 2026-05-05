package com.example.snkrsapp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario
import com.example.snkrsapp.Domain.ModelActualizarPerfil
import com.example.snkrsapp.Domain.ModelPerfil
import com.example.snkrsapp.Views.Pantallas.PantallaActualizarPerfil
import com.example.snkrsapp.Views.ViewModels.ActualizarPerfilViewModel
import com.example.snkrsapp.Views.ViewModels.PerfilViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PantallaActualizarPerfilTestUI {

    @get:Rule
    val composeRule = createComposeRule()
    val viewModelPerfil = mockk<PerfilViewModel>(relaxed = true)
    val viewModel = mockk<ActualizarPerfilViewModel>(relaxed = true)
    val usuario = Usuario("1", "Jonathan", "email", "apellidos", "2005-01-01")
    val estadoInicial = MutableStateFlow(ModelActualizarPerfil(usuario, false, false))
    val estadoPerfil = MutableStateFlow(ModelPerfil(usuario, false, false))
    fun lanzarPantallaActualizarPerfil(onCerrarSesion: () -> Unit = {}) {


        every { viewModel.model } returns estadoInicial
        every { viewModelPerfil.model } returns estadoPerfil

        composeRule.setContent {
            PantallaActualizarPerfil({}, onCerrarSesion, viewModelPerfil, viewModel)
        }
    }


    @Test
    fun verificar_elementos_iniciales_existen() {
        lanzarPantallaActualizarPerfil()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("NombreUsuario").assertExists()
        composeRule.onNodeWithText("Editar").assertExists()
        composeRule.onNodeWithTag("CardPerfil").assertExists()
    }

    @Test
    fun al_pulsar_editar_aparecen_campos_texto() {
        lanzarPantallaActualizarPerfil()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Editar").performClick()
        composeRule.onNodeWithText("Editar").assertDoesNotExist()
        composeRule.onNodeWithText("Nombre de usuario").assertExists()
    }

    @Test
    fun al_pulsar_cerrar_sesion_se_ejecuta_la_accion() {
        var sesionCerrada = false

        lanzarPantallaActualizarPerfil(onCerrarSesion = { sesionCerrada = true })
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("BotonCerrarSesion").performScrollTo().performClick()
        composeRule.waitForIdle()

        assert(sesionCerrada)
    }
}