package com.example.snkrsapp.Views.Controlador

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.snkrsapp.Views.Controlador.NavigationUtils.BotonCrearEvento
import com.example.snkrsapp.Views.Controlador.NavigationUtils.BottomBar
import com.example.snkrsapp.Views.Pantallas.PantallaActualizarPerfil
import com.example.snkrsapp.Views.Pantallas.PantallaAgregarProducto
import com.example.snkrsapp.Views.Pantallas.PantallaEventos
import com.example.snkrsapp.Views.Pantallas.PantallaInicioSesion
import com.example.snkrsapp.Views.Pantallas.PantallaListados
import com.example.snkrsapp.Views.Pantallas.PantallaPerfil
import com.example.snkrsapp.Views.Pantallas.PantallaPrincipal
import com.example.snkrsapp.Views.Pantallas.PantallaProductoDetallado
import com.example.snkrsapp.Views.Pantallas.PantallaRegistro
import com.example.snkrsapp.Views.ViewModels.ActualizarPerfilViewModel
import com.example.snkrsapp.Views.ViewModels.EventosViewModel
import com.example.snkrsapp.Views.ViewModels.InicioSesionViewModel
import com.example.snkrsapp.Views.ViewModels.ListadoViewModel
import com.example.snkrsapp.Views.ViewModels.PerfilViewModel
import com.example.snkrsapp.Views.ViewModels.PrincipalViewModel
import com.example.snkrsapp.Views.ViewModels.ProductoDetalladoViewModel
import com.example.snkrsapp.Views.ViewModels.RegistroViewModel
import com.example.snkrsapp.Views.ViewModels.ViewmodelAgregarProducto

@Composable
fun Controlador() {

    val navController = rememberNavController()

    val iniSesViewModel: InicioSesionViewModel = hiltViewModel()
    val registroViewModel: RegistroViewModel = hiltViewModel()
    val principalViewModel: PrincipalViewModel = hiltViewModel()
    val productoDetalladoViewModel: ProductoDetalladoViewModel = hiltViewModel()
    val perfilViewModel: PerfilViewModel = hiltViewModel()
    val actuViewModel: ActualizarPerfilViewModel = hiltViewModel()
    val evenViewModel: EventosViewModel = hiltViewModel()
    val agregarProductoViewModel: ViewmodelAgregarProducto = hiltViewModel()
    val listadoViewModel: ListadoViewModel = hiltViewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val listaSinBottombar = listOf("InicioSesion", "Registro", "AgregarProducto")
    var mostrarSheet by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            if (currentRoute !in listaSinBottombar) {
                BottomBar(navController)
            }
        },
        floatingActionButton = {
            if (currentRoute == "Eventos") {
                BotonCrearEvento(
                    navController
                ) { mostrarSheet = true }
            }
        }

    ) { paddingValues ->
        NavHost(navController = navController, startDestination = "InicioSesion") {
            composable("InicioSesion") {
                PantallaInicioSesion(
                    iniSesViewModel,
                    { navController.navigate("Registro") },
                    { navController.navigate("Principal") })
            }
            composable("Registro") {
                PantallaRegistro(registroViewModel)
            }
            composable("Principal") {
                PantallaPrincipal(
                    principalViewModel,
                    { navController.navigate("ProductoDetallado") })
            }
            composable("ProductoDetallado") {
                PantallaProductoDetallado(
                    14,
                    1,
                    { navController.navigate("Principal") },
                    productoDetalladoViewModel
                )
            }

            composable("Perfil") {
                PantallaPerfil(
                    { navController.navigate("ActualizarPerfil") },
                    perfilViewModel,
                    { navController.navigate("Listados/${it}") })
            }
            composable("ActualizarPerfil") {
                PantallaActualizarPerfil(
                    { navController.navigate("Perfil") },
                    {},
                    perfilViewModel,
                    actuViewModel
                )
            }
            composable("Eventos") {
                PantallaEventos(evenViewModel, paddingValues, mostrarSheet, { mostrarSheet = it })
            }

            composable("AgregarProducto") {
                PantallaAgregarProducto(
                    { navController.navigate("Principal") },
                    principalViewModel,
                    agregarProductoViewModel,
                    paddingValues
                )
            }

            composable("Listados/{id}") {
                val id = it.arguments?.getString("id")?.toIntOrNull() ?: 0
                PantallaListados(
                    { navController.navigate("Perfil") },
                    listadoViewModel,
                    paddingValues,
                    id
                )
            }
        }
    }

}