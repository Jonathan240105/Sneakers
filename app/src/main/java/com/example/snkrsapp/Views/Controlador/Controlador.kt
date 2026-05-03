package com.example.snkrsapp.Views.Controlador

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snkrsapp.Data.RemoteData.AutorizacionDao.Usuario
import com.example.snkrsapp.Views.Pantallas.PantallaInicioSesion
import com.example.snkrsapp.Views.Pantallas.PantallaPerfil
import com.example.snkrsapp.Views.Pantallas.PantallaPrincipal
import com.example.snkrsapp.Views.Pantallas.PantallaProductoDetallado
import com.example.snkrsapp.Views.Pantallas.PantallaRegistro
import com.example.snkrsapp.Views.ViewModels.InicioSesionViewModel
import com.example.snkrsapp.Views.ViewModels.PerfilViewModel
import com.example.snkrsapp.Views.ViewModels.PrincipalViewModel
import com.example.snkrsapp.Views.ViewModels.ProductoDetalladoViewModel
import com.example.snkrsapp.Views.ViewModels.RegistroViewModel

@Composable
fun Controlador() {

    val navController = rememberNavController()

    val iniSesViewModel: InicioSesionViewModel = hiltViewModel()
    val registroViewModel: RegistroViewModel = hiltViewModel()
    val principalViewModel: PrincipalViewModel = hiltViewModel()
    val productoDetalladoViewModel: ProductoDetalladoViewModel = hiltViewModel()
    val perfilViewModel: PerfilViewModel = hiltViewModel()



    NavHost(navController = navController, startDestination = "InicioSesion") {
        composable("InicioSesion") {
            PantallaInicioSesion(iniSesViewModel, {navController.navigate("Registro")},{ navController.navigate("Perfil") })
        }
        composable("Registro") {
            PantallaRegistro(registroViewModel)
        }
        composable("Principal") {
            PantallaPrincipal(principalViewModel, { navController.navigate("ProductoDetallado") })
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
            PantallaPerfil({}, perfilViewModel)
        }
    }

}