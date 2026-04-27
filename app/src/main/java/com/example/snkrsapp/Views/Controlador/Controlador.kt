package com.example.snkrsapp.Views.Controlador

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snkrsapp.Views.Pantallas.PantallaInicioSesion
import com.example.snkrsapp.Views.Pantallas.PantallaPrincipal
import com.example.snkrsapp.Views.Pantallas.PantallaRegistro
import com.example.snkrsapp.Views.ViewModels.InicioSesionViewModel
import com.example.snkrsapp.Views.ViewModels.PrincipalViewModel
import com.example.snkrsapp.Views.ViewModels.RegistroViewModel

@Composable
fun Controlador() {

    val navController = rememberNavController()

    val iniSesViewModel: InicioSesionViewModel = hiltViewModel()
    val registroViewModel: RegistroViewModel = hiltViewModel()
    val principalViewModel: PrincipalViewModel = hiltViewModel()


    NavHost(navController = navController, startDestination = "Principal") {
        composable("InicioSesion") {
            PantallaInicioSesion(iniSesViewModel, {})
        }
        composable("registro") {
            PantallaRegistro(registroViewModel)
        }
        composable("Principal") {
            PantallaPrincipal(principalViewModel)
        }
    }

}