package com.example.snkrsapp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.snkrsapp.Domain.Marca
import com.example.snkrsapp.Domain.ModelProductoDetallado
import com.example.snkrsapp.Domain.Producto
import com.example.snkrsapp.Views.Pantallas.PantallaProductoDetallado
import com.example.snkrsapp.Views.ViewModels.ProductoDetalladoViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PantallaProductoDetalladoTestUI {

    @get:Rule
    val composeRule = createComposeRule()


    fun lanzarPantallaProductoDetallado() {
        val viewModel = mockk<ProductoDetalladoViewModel>(relaxed = true)
        val estadoInicial = MutableStateFlow(
            ModelProductoDetallado(
                Producto(
                    1,
                    1,
                    "",
                    100,
                    40
                ),
                Marca(
                    1, "Nike", "url"
                )
            )
        )

        every { viewModel.model } returns estadoInicial
        composeRule.setContent {
            every { viewModel.cargarProducto(any()) } returns Unit
            every { viewModel.cargarMarca(any()) } returns Unit
            PantallaProductoDetallado(1, 1, {}, viewModel)
        }
    }

    @Test
    fun Comprobar_componentes_principales_existen() {
        lanzarPantallaProductoDetallado()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("imagenProducto").assertExists()
        composeRule.onNodeWithTag("modelo").assertExists()
        composeRule.onNodeWithText("Detalles").assertExists()
        composeRule.onNodeWithText("Información").assertExists()
    }

    @Test
    fun Comprobar_CambioDeInformacion_al_pulsar_botonTab() {
        lanzarPantallaProductoDetallado()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Información").performClick()
        composeRule.onNodeWithText("Nike").assertExists()
    }
}
