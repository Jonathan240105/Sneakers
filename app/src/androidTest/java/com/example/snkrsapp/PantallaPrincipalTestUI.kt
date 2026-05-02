package com.example.snkrsapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.snkrsapp.Domain.ModelPrincipal
import com.example.snkrsapp.Domain.Producto
import com.example.snkrsapp.Views.Pantallas.PantallaPrincipal
import com.example.snkrsapp.Views.ViewModels.PrincipalViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PantallaPrincipalTestUI {

    @get:Rule
    val composeRule = createComposeRule()

    //Función para lanzar la pantalla principal
    fun lanzarPantallaPrincipal(
        productos: List<Producto> = emptyList(), estaCargando: Boolean = false
    ) {
        val viewmodel = mockk<PrincipalViewModel>()
        val estadoInicial = MutableStateFlow(
            ModelPrincipal(
                listaDeproductos = productos, exitoProductos = false, cargandoProductos = estaCargando
            )
        )

        composeRule.setContent {
            every {
                viewmodel.model
            } returns estadoInicial.asStateFlow()
            PantallaPrincipal(viewmodel,{})
        }
    }

    //Función para comprobar que el título existe en la pantalla y que muestra lo esperado
    @Test
    fun cuandoHayProductos_SeMuestranEnPantalla() {
        val misZapas = listOf(
            Producto(modelo = "Jordan", idMarca = 1, precio = 100, talla = 42)
        )

        lanzarPantallaPrincipal(productos = misZapas)
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Jordan").assertIsDisplayed()
        composeRule.onNodeWithText("100 €").assertIsDisplayed()
    }

    //Función que comprueba que el botón de filtros funciona correctamente y el flujo se completa
    @Test
    fun Comprobar_boton_filtros_funciona() {

        lanzarPantallaPrincipal()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("botonFiltros").performClick()

        composeRule.onNodeWithText("42").assertIsDisplayed()
        composeRule.onNodeWithText("42").performClick()

        composeRule.onNodeWithTag("botonAplicarFiltros").performClick()
        composeRule.onNodeWithTag("botonAplicarFiltros").assertDoesNotExist()
    }

    //Función que comprueba que el campo de búsqueda funciona correctamente
    @Test
    fun alEscribirEnBuscador_SeActualizaElTexto() {
        lanzarPantallaPrincipal()
        composeRule.waitForIdle()

        val textoBusqueda = "Adidas Forum"

        composeRule.onNodeWithTag("TextFieldZapatillas")
            .performTextInput(textoBusqueda)

        composeRule.onNodeWithText(textoBusqueda).assertExists()
    }
}