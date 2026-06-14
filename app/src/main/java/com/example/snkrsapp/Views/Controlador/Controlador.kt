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
import com.example.snkrsapp.Views.Pantallas.PantallaEventosAdmin
import com.example.snkrsapp.Views.Pantallas.PantallaInicioSesion
import com.example.snkrsapp.Views.Pantallas.PantallaListados
import com.example.snkrsapp.Views.Pantallas.PantallaMarcasAdmin
import com.example.snkrsapp.Views.Pantallas.PantallaPerfil
import com.example.snkrsapp.Views.Pantallas.PantallaPerfilAdmin
import com.example.snkrsapp.Views.Pantallas.PantallaPrincipal
import com.example.snkrsapp.Views.Pantallas.PantallaProductoDetallado
import com.example.snkrsapp.Views.Pantallas.PantallaRegistro
import com.example.snkrsapp.Views.Pantallas.PantallaUsuariosAdmin
import com.example.snkrsapp.Views.ViewModels.ActualizarPerfilViewModel
import com.example.snkrsapp.Views.ViewModels.EventosViewModel
import com.example.snkrsapp.Views.ViewModels.InicioSesionViewModel
import com.example.snkrsapp.Views.ViewModels.ListadoViewModel
import com.example.snkrsapp.Views.ViewModels.MarcasAdminViewModel
import com.example.snkrsapp.Views.ViewModels.PantallaEventosViewModel
import com.example.snkrsapp.Views.ViewModels.PerfilViewModel
import com.example.snkrsapp.Views.ViewModels.PrincipalViewModel
import com.example.snkrsapp.Views.ViewModels.ProductoDetalladoViewModel
import com.example.snkrsapp.Views.ViewModels.RegistroViewModel
import com.example.snkrsapp.Views.ViewModels.UsuariosAdminViewModel
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
    val usuariosAdminViewModel: UsuariosAdminViewModel = hiltViewModel()
    val marcasAdminViewModel: MarcasAdminViewModel = hiltViewModel()
    val eventosAdminViewModel: PantallaEventosViewModel = hiltViewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val listaSinBottombar =
        listOf(
            "InicioSesion",
            "Registro",
            "AgregarProducto",
            "ProductoDetallado/{id}/{marca}",
            "Perfil/{uid}",
            "Listados/{id}/{uid}",
            "UsuariosAdmin",
            "MarcasAdmin",
            "EventosAdmin",
            "PerfilAdmin"
        )
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
                    {
                        navController.navigate("Principal") {
                            popUpTo("InicioSesion") {
                                inclusive = true
                            }
                        }
                    },
                    {
                        navController.navigate("UsuariosAdmin") {
                            popUpTo("InicioSesion") {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable("UsuariosAdmin") {
                PantallaUsuariosAdmin(usuariosAdminViewModel, paddingValues, navController)
            }
            composable("MarcasAdmin") {
                PantallaMarcasAdmin(
                    marcasAdminViewModel,
                    agregarProductoViewModel,
                    paddingValues,
                    navController
                )
            }
            composable("EventosAdmin") {
                PantallaEventosAdmin(eventosAdminViewModel, paddingValues, navController)
            }
            composable("PerfilAdmin") {
                PantallaPerfilAdmin(
                    {},
                    actuViewModel,
                    { navController.navigate("InicioSesion") { popUpTo(0) { inclusive = true } } },
                    perfilViewModel,
                    navController
                )
            }
            composable("Registro") {
                PantallaRegistro(
                    registroViewModel,
                    agregarProductoViewModel,
                    { navController.navigate("InicioSesion") })
            }
            composable("Principal") {
                PantallaPrincipal(
                    principalViewModel,
                    { pr, m -> navController.navigate("ProductoDetallado/${pr}/${m}") })
            }
            composable("ProductoDetallado/{id}/{marca}") {
                val id = it.arguments?.getString("id")?.toIntOrNull() ?: 0
                val marca = it.arguments?.getString("marca")?.toIntOrNull() ?: 0
                PantallaProductoDetallado(
                    id,
                    marca,
                    { navController.navigate("Principal") },
                    productoDetalladoViewModel,
                    paddingValues,
                    { navController.navigate("Perfil/${it}") }
                )
            }

            composable("Perfil") {
                PantallaPerfil(
                    null,
                    { navController.navigate("ActualizarPerfil") },
                    {},
                    perfilViewModel,
                    { navController.navigate("Listados/${it}") })
            }
            composable(
                "Perfil/{uid}",
            ) {
                val uidVendedor = it.arguments?.getString("uid")
                PantallaPerfil(
                    uidPerfilAVisualizar = uidVendedor,
                    cambiarAConfig = { },
                    { navController.popBackStack() },
                    myViewModel = perfilViewModel,
                    navegarAListado = { navController.navigate("Listados/${it}/${uidVendedor}") }
                )
            }
            composable("ActualizarPerfil") {
                PantallaActualizarPerfil(
                    { navController.navigate("Perfil") },
                    { navController.navigate("InicioSesion") { popUpTo(0) { inclusive = true } } },
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
                    id,
                    null
                )
            }
            composable("Listados/{id}/{uid}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
                val uid = backStackEntry.arguments?.getString("uid")

                PantallaListados(
                    navegarADetalle = { navController.popBackStack() },
                    myViewModel = listadoViewModel,
                    paddingValues = paddingValues,
                    id = id,
                    uid = uid
                )
            }
        }
    }
}